<template>
    <div class="small">
        <pie-chart :chart-data="datacollection"></pie-chart>
        <button @click="fillData">Randomize</button>
    </div>
</template>

<script>
import PieChart from './../../charts/LineChart.js'
import axios from 'axios'
//import DoughnutChart from 'vue-doughnut-chart'
import PieChart from './../../charts/PieChart.js'

export default {
    components: {
        //LineChart,
        PieChart
        PieChart
    },
    data () {
        return {
            datacollection: null,
            paises: [],
            hola: {}
            datacollection: null,
            dataDeportes: [40,40,65,76,12,24,33],
            loading: true
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
                labels: ["Boxeo","Fútbol Femenino", "Tenis","Natación","Volley Ball","Rugby","Basquetball",],
                datasets: [
                    {
                        label: 'Data One',
                        backgroundColor: ['#333', '#7FFF00', '#00008B', '#8B008B'],
                        data: [this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt()]
                    backgroundColor:['green','red','yellow','black','orange','blue','violet'],
                    data: this.dataDeportes
                    }
                ]
            }
        },
        getRandomInt () {
            return Math.floor(Math.random() * (50 - 5 + 1)) + 5
        }
        getData(){
            axios
                .get('URL')
                .then(response=>(this.dataDeportes=response.data))
                .catch(error=>{console.log(error)})
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
