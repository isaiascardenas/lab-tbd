<template>
  <div class="small" v-loading="loading">
    <div class="text">Cantidad de tweets deportes por país</div>
    <bar-chart :chart-data="datacollection"></bar-chart>
  </div>
</template>

<script>
import BarChart from './../../charts/HorizontalBarChart.js';
import { PaisesResources, DeportesPaisResources, multipleRequest, DeportesResources } from './../../router/endpoints';

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
      DeportesPaisResources.get({ pais_id: 1 })
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
    methods: {
        getData() {
            let self = this;
            let promises = [];
            PaisesResources.get({})
                .then((response) => {

                    self.paises = response.data;
                    _.each(self.paises, (country) => {
                        promises.push(DeportesPaisResources.getPaisesEstadisticas({ pais_id: country.countryId }));
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
      let self = this;
      let labels = _.map(this.deportes, (sport) => {
        return sport.sport.sportName;
      });
      let values = _.map(this.deportes, (sport) => {
        return sport.statisticCount;
      });
      console.log(labels);
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
