<template>
    <div class="small" v-loading="loading">
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
            console.log(this.fechas);

            let self = this;
            let labels = _.map(this.fechas, (day) => {
                return day.dateName
            });
            let values = _.map(this.fechas, (day) => {
                return day.statistics.length;
            });


            this.datacollection = {
                labels: labels,
                datasets: [
                    {
                        label: 'Data One',
                        backgroundColor: ['#9C27B0','#FFC107','#CDDC39','#8BC34A','#607D8B','#9E9E9E','#00BCD4', '#536DFE', '#C2185B'],
                        data: values
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
    max-width: 460px;
    margin-left: auto;
    margin-right: auto;
}
</style>
