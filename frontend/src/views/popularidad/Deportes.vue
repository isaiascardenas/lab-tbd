<template>
    <div class="small">
        <div class="text">Cantidad de tweets por deportes</div>
        <bar-chart :chart-data="datacollection"></bar-chart>
    </div>
</template>

<script>

import axios from 'axios'

import BarChart from './../../charts/HorizontalBarChart.js'

export default {
    components: {
        BarChart
    },
    data () {
        return {
	    datacollection: null,
            dataDeportes: [40,40,65,76,12,24,33],
            loading: true
        }
    },
    mounted () {
        //this.getData()
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
        getData(){
            axios.get('URL DATA POR DEPORTE')
                .then(function (response) {
                    console.log(response.data);
                    this.dataDeportes = response.data;
                })
                .catch(function (error) {
                    console.log(error);
                })
                .finally(()=>this.loading=false)
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
