<template>
    <div class="small" v-loading="loading">
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
            datacollection: {},
            deportes: [],
            loading: true
        }
    },
    mounted () {
        this.getDeportes();
    },
    methods: {
        getDeportes() {
            let self = this;
            DeportesResources.get({})
                .then((response) => {
                    self.deportes = response.data;
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
            let self = this;
            let labels = _.map(this.deportes, (sport) => {
                return sport.sportName
            });
            let values = _.map(this.deportes, (sport) => {
                return sport.statistics[19].statisticCount;
            });
            console.log(values);

            this.datacollection = {
                labels: labels,
                datasets: [
                    {
                        label: [],
                        backgroundColor: ['#536DFE','#FFC107','#CDDC39','#8BC34A','#607D8B','#9E9E9E','#00BCD4', '#9C27B0', '#C2185B'],
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
