<template>
    <div class="small" v-loading="loading">
        <div class="text">Cantidad de tweets por pais</div>
        <pie-chart :chart-data="datacollection" :options="opciones"></pie-chart>
    </div>
</template>

<script>
import PieChart from './../../charts/PieChart.js'
import { PaisesResources } from './../../router/endpoints';

export default {
    components: {
        PieChart
    },
    data () {
        return {
            datacollection: null,
            paises: [],
            loading: true,
            opciones:{
                tooltips: {
                    callbacks: {
                        title: function(tooltipItem, data) {
                            return data['labels'][tooltipItem[0]['index']];
                        },
                        label: function(tooltipItem, data) {
                            return 'Total: '+data['datasets'][0]['data'][tooltipItem['index']]+' ('+data['datasets'][0]['notuseful'][tooltipItem['index']]+'%)';
                        },
                    },
                backgroundColor: '#000',
                titleFontSize: 16,
                titleFontColor: '#FFF',
                bodyFontColor: '#FFF',
                bodyFontSize: 14,
                displayColors: true
                }
            }
        }
    },
    mounted () {
        this.getPaises();
    },
    methods: {
        getPaises () {
            var self = this;
            PaisesResources.get({})
                .then((response) => {
                    self.paises = response.data;
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

            let labels = _.map(this.paises, (country) => {
                return country.countryName
            });
            let values = _.map(this.paises, (country) => {
                return country.countryTweetCount
            });
            let total= _.reduce(values,function(sum,n){
                return sum+n;
            });
            let percentage=_.transform(values,function(result,n){
                result.push(Math.round(n*100/total*10)/10);
                return result;
            });
            this.datacollection = {
                labels: labels,
                datasets: [
                    {
                        data: values,
                        notuseful: percentage,
                        backgroundColor: ['#9C27B0','#FFC107','#CDDC39','#8BC34A','#607D8B','#9E9E9E','#00BCD4', '#536DFE', '#C2185B'],
                        
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
