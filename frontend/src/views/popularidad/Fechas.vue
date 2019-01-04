<template>
  <div class="small" v-loading="loading">
    <div class="text">Cantidad de tweets por fechas</div>
    <line-chart :chart-data="datacollection"></line-chart>
  </div>
</template>

<script>
import LineChart from './../../charts/LineChart.js';
import { FechasResources } from './../../router/endpoints';

export default {
  components: {
    LineChart,
  },
  data() {
    return {
      datacollection: null,
      statistics: [],
      loading: true,
    };
  },
  mounted() {
    this.getStatistics();
  },
  methods: {
    getStatistics() {
      var self = this;
      FechasResources.get({})
        .then(response => {
          self.statistics = response.data;
          self.fillData();
        })
        .catch(error => {
          console.log(error);
        })
        .finally(() => {
          this.loading = false;
        });
    },
    fillData() {
      let self = this;
      let labels = _.map(this.statistics, day => {
        return day.fechaValue;
      });
      let values = _.map(this.statistics, day => {
        return day.fechaCount;
      });

      this.datacollection = {
        labels: labels,
        datasets: [
          {
            label: 'Deportes',
            backgroundColor: ['#e2431e'],
            fill: false,
            borderColor: ['Red'],
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
