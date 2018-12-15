<template>
  <div class="small">
    <div class="text">Cantidad de tweets por pais</div>
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
        axios.get('http://localhost:4040/country/all')
            .then((response) => {
                console.log(response.data, 'youtino')
                this.paises = response.data;
                this.fillData();
            });
    },
    methods: {
        getPaises () {
            var self = this;
            axios.get('http://localhost:4040/country/all')
                .then(function (response) {
                    self.paises = response.data;
                    console.log(self.paises);
                })
                .catch(function (error) {
                    console.log(error);
                });
        },
        fillData () {
            console.log(this.paises, 'Youta');
            this.datacollection = {
                labels: [this.paises[0].countryName, this.paises[1].countryName, this.paises[2].countryName, 'Venezuela'],
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
