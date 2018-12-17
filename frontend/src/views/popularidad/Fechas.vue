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
            statistics: [],
            loading: true,
        }
    },
    mounted () {
        this.getStatistics();
    },
    methods: {
        getStatistics () {
            var self = this;
            FechasResources.get({})
                .then((response) => {
                    self.statistics = response.data;
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
            console.log(this.statistics);

            let self = this;
            let labels = _.map(this.statistics, (day) => {
                return day.statisticQuery
            });
            let values = _.map(this.statistics, (day) => {
                return day.statisticDate;
            });
            console.log(values);
            this.datacollection = {
                labels: ['2018-12-06', '2018-12-07', '2018-12-08','2018-12-09','2018-12-10','2018-12-11', '2018-12-12', '2018-12-13', '2018-12-14', '2018-12-15', '2018-12-16'],
                datasets: [
                    {
                        label: 'Deportes',
                        backgroundColor: ['#e2431e'],
                        fill: false,
                        borderColor: ['Red'],
                        data: [30, 60, 70, 100, 30, 40, 90, 60, 70, 80, 100, 120]
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
