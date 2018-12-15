<template>
    <div class="small">
        <pie-chart :chart-data="datacollection"></pie-chart>
        <button @click="fillData">Randomize</button>
    </div>
</template>

<script>
import PieChart from './../../charts/PieChart.js'

export default {
    components: {
        PieChart
    },
    data () {
        return {
            datacollection: null,
            dataDeportes: [40,40,65,76,12,24,33],
            loading: true
        }
    },
    mounted () {
        this.fillData()
    },
    methods: {
        fillData () {
            this.datacollection = {
                labels: ["Boxeo","Fútbol Femenino", "Tenis","Natación","Volley Ball","Rugby","Basquetball",],
                datasets: [
                    {
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
