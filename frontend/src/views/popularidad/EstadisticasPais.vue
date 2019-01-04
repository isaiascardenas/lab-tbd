<template>
  <div class="small" v-loading="loading">
    <div class="text">Cantidad de tweets sobre deportes en {{ paisName }}</div>
    <bar-chart :chart-data="datacollection"></bar-chart>
  </div>
</template>

<script>
import BarChart from './../../charts/HorizontalBarChart.js';
import { EstadisticasResources } from './../../router/endpoints';

export default {
  components: {
    BarChart,
  },
  data() {
    return {
      datacollection: {},
      deportes: [],
      paisName: '',
      loading: true,
    };
  },
  mounted() {
    this.getEstadisticas();
  },
  methods: {
    getEstadisticas() {
      EstadisticasResources.getPais({ pais_id: this.$route.params.id })
        .then(response => {
          this.paisName = response.data[0].country.countryName;
          this.deportes = response.data;
          this.fillData();
          this.loading = false;
        })
        .catch(error => {
          console.log(error);
        });
    },
    fillData() {
      let self = this;
      let labels = _.map(this.deportes, sport => {
        return sport.sport.sportName;
      });

      let values = _.map(this.deportes, sport => {
        return sport.statisticCount;
      });
      console.log(labels);
      console.log(values);

      this.datacollection = {
        labels: labels,
        datasets: [
          {
            label: 'Deporte',
            backgroundColor: [
              '#536DFE',
              '#FFC107',
              '#CDDC39',
              '#8BC34A',
              '#607D8B',
              '#9E9E9E',
              '#00BCD4',
            ],
            data: values,
          },
        ],
      };
    },
  },
};
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
