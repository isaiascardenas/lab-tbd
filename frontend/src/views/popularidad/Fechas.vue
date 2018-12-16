<template>
    <div class="small" :loading="loading">
        <div class="text">Cantidad de tweets por fechas</div>
        <line-chart :chart-data="datacollection"></line-chart>
    </div>
</template>

<script>
import LineChart from './../../charts/LineChart.js'
import { FechasResources } from './../../router/endpoints';

export default {
    components: {
        LineChart
    },
    data () {
        return {
            datacollection: null,
            fechas: [],
            loading: true,
        }
    },
    mounted () {
        this.getFechas();
    },
    methods: {
        getFechas () {
            var self = this;
            FechasResources.get({})
                .then((response) => {
                    self.fechas = response.data;
                    console.log(self.fechas );
                    self.fillData();
                })
                .catch((error) => {
                    console.log(error);
                })
                .finally(() => {
                    this.loading = false;
                });

        },
        fillData () {
            console.log(this.fechas , 'Youta');
            this.datacollection = {
                labels: [this.fechas [0].countryName, this.fechas [1].countryName, this.fechas [2].countryName, 'Venezuela'],
                datasets: [
                    {
                        label: 'Data One',
                        backgroundColor: ['#333', '#7FFF00', '#00008B', '#8B008B'],
                        data: [this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt()]
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
