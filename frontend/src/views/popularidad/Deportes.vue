<template>
    <div class="small" :loading="loading">
        <div class="text">Cantidad de tweets por deportes</div>
        <bar-chart :chart-data="datacollection"></bar-chart>
    </div>
</template>

<script>
import BarChart from './../../charts/HorizontalBarChart.js';
import { DeportesResources } from './../../router/endpoints';

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
        console.log(DeportesResources);
        this.getDeportes();
    },
    methods: {
        getDeportes() {
            let self = this;
            DeportesResources.get({})
                .then((response) => {
                    self.dataDeportes = response.data;
                    console.log(response.data);
                    self.fillData();
                })
                .catch((error) => {
                    console.log(error);
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        fillData() {
            this.datacollection = {
                labels: ["Boxeo","Fútbol Femenino", "Tenis","Natación","Volley Ball","Rugby","Basquetball",],
                datasets: [
                    {
                        backgroundColor:['green','red','yellow','black','orange','blue','violet'],
                        data: this.dataDeportes
                    }
                ]
            }
        }
    }
}
</script>

<style>
.small {
    margin-top: 20px;
    margin-bottom: 20px;
    max-width: 520px;
    margin-left: auto;
    margin-right: auto;
}
</style>
