<template>
    <div class="small" v-loading="loading">
        <div class="text">Cantidad de tweets deportes por país</div>
        <bar-chart :chart-data="datacollection"></bar-chart>
    </div>
</template>

<script>
import BarChart from './../../charts/HorizontalBarChart.js';
import { PaisesResources, DeportesPaisResources, multipleRequest } from './../../router/endpoints';

export default {
    components: {
        BarChart
    },
    data () {
        return {
            datacollection: {},
            paises: [],
            data: [],
            loading: true
        }
    },
    mounted () {
        this.getData();
    },
    methods: {
        getData() {
            let self = this;
            let promises = [];
            PaisesResources.get({})
                .then((response) => {

                    self.paises = response.data;
                    _.each(self.paises, (country) => {
                        promises.push(DeportesPaisResources.get({ pais_id: country.countryId }));
                    });

                    multipleRequest.all(promises)
                    .then((res) => {
                        _.each(res, (response) => {
                            self.data.push(response.data);
                        });
                        self.fillData();
                    })
                    .catch((error) => {
                        console.log(error);
                    })
                    .finally(() => {
                        self.loading = false;
                    });

                })
                .catch((error) => {
                    console.log(error);
                    this.loading = false;
                })

        },
        fillData() {

            console.log('data', this.data);

            let self = this;
            let labels = _.map(this.paises, (sport) => {
                return sport.countryName
            });

            // maybe _.groupBy
            let values = _.map(this.data, (statistic) => {
                return statistic.statisticCount;
            });

            this.datacollection = {
                labels: labels,
                datasets: [
                    {
                        // Este es un deporte
                        label: 'Data One',
                        // aca va el color del deporte
                        backgroundColor: ['#9C27B0','#FFC107','#CDDC39','#8BC34A','#607D8B','#9E9E9E','#00BCD4'],
                        // aca van los valores del deporte, para cada pais
                        data: [10, 11, 12, 20, 14, 15, 16, 17]
                    }, {
                        label: 'Data Two',
                        backgroundColor: ['#9C27B0','#FFC107','#CDDC39','#8BC34A','#607D8B','#9E9E9E','#00BCD4'],
                        data: [10, 11, 12, 13, 14, 15, 16, 17]
                    }, {
                        label: 'Data Three',
                        backgroundColor: ['#9C27B0','#FFC107','#CDDC39','#8BC34A','#607D8B','#9E9E9E','#00BCD4'],
                        data: [10, 11, 12, 13, 14, 15, 16, 17]
                    }, {
                        label: 'Data Four',
                        backgroundColor: ['#9C27B0','#FFC107','#CDDC39','#8BC34A','#607D8B','#9E9E9E','#00BCD4'],
                        data: [10, 11, 12, 13, 14, 15, 16, 17]
                    }, {
                        label: 'Data Five',
                        backgroundColor: ['#9C27B0','#FFC107','#CDDC39','#8BC34A','#607D8B','#9E9E9E','#00BCD4'],
                        data: [10, 11, 12, 13, 14, 15, 16, 17]
                    }, {
                        label: 'Data Six',
                        backgroundColor: ['#9C27B0','#FFC107','#CDDC39','#8BC34A','#607D8B','#9E9E9E','#00BCD4'],
                        data: [10, 11, 12, 13, 14, 15, 16, 17]
                    }, {
                        label: 'Data Seven',
                        backgroundColor: ['#9C27B0','#FFC107','#CDDC39','#8BC34A','#607D8B','#9E9E9E','#00BCD4'],
                        data: [10, 11, 12, 13, 14, 15, 16, 17]
                    }, {
                        label: 'Data Eight',
                        backgroundColor: ['#9C27B0','#FFC107','#CDDC39','#8BC34A','#607D8B','#9E9E9E','#00BCD4'],
                        data: [10, 11, 12, 13, 14, 15, 16, 17]
                    }, {
                        label: 'Data Nine',
                        backgroundColor: ['#9C27B0','#FFC107','#CDDC39','#8BC34A','#607D8B','#9E9E9E','#00BCD4'],
                        data: [10, 11, 12, 13, 14, 15, 16, 17]
                    }
                    // {
                        // label: [],
                        // backgroundColor: ['#536DFE','#FFC107','#CDDC39','#8BC34A','#607D8B','#9E9E9E','#00BCD4', '#9C27B0', '#C2185B'],
                        // data: values
                    // }
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
