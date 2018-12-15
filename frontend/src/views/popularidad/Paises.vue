<template>
  <div class="small">
    <pie-chart :chart-data="datacollection"></pie-chart>
    <button @click="fillData">Randomize</button>
  </div>
</template>

<script>
import PieChart from './../../charts/PieChart.js'
import axios from 'axios'
//import DoughnutChart from 'vue-doughnut-chart'

export default {
    components: {
        PieChart
    },
    data () {
        return {
            datacollection: null,
            paises: [],
            hola: {}
        }
    },
    mounted () {
        this.getPaises()
        this.fillData()
    },
    methods: {
        getPaises () {
            var self = this;
            axios.get('http://localhost:4040/country/all')
                .then(function (response) {
                    console.log(response.data);
                    self.paises = response.data;
                    console.log(self.paises);
                })
                .catch(function (error) {
                    console.log(error);
                });
        },
        fillData () {
            this.datacollection = {
                labels: ['Argentina', 'Chile', 'Mexico', 'Venezuela'],
                datasets: [
                    {
                        label: 'Data One',
                        backgroundColor: ['#333', '#7FFF00', '#00008B', '#8B008B'],
                        data: [this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt()]
                    }
                ]
            }
        },
        getRandomInt () {
            return Math.floor(Math.random() * (50 - 5 + 1)) + 5
        }
    }
}
</script>

<style>
  .small {
    max-width: 600px;
    margin:  150px auto;
  }
</style>
