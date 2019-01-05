<template>
  <div class="small" v-loading="loading">
    <div class="text">Cantidad de tweets por fechas</div>
    <line-chart :chart-data="datacollection" :options="opciones"></line-chart>
  </div>
</template>

<script>
import LineChart from "./../../charts/LineChart.js";
import { FechasResources } from "./../../router/endpoints";

export default {
  components: {
    LineChart
  },
  data() {
    return {
      datacollection: null,
      statistics: [],
      loading: true,
      opciones: {
        tooltips: {
          callbacks: {
            title: function(tooltipItem, data) {
              return data["labels"][tooltipItem[0]["index"]];
            },
            label: function(tooltipItem, data) {
              return (
                "Total: " +
                data["datasets"][0]["data"][tooltipItem["index"]] +
                " (" +
                data["datasets"][0]["notuseful"][tooltipItem["index"]] +
                "%)"
              );
            }
          },
          backgroundColor: "#000",
          titleFontSize: 16,
          titleFontColor: "#FFF",
          bodyFontColor: "#FFF",
          bodyFontSize: 14,
          displayColors: false
        },
        legend: {
          display: false
        }
      }
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
      let total = _.reduce(values, function(sum, n) {
        return sum + n;
      });
      let percentage = _.transform(values, function(result, n) {
        result.push(Math.round(((n * 100) / total) * 10) / 10);
        return result;
      });
      this.datacollection = {
        labels: labels,
        datasets: [
          {
            label: "Deportes",
            backgroundColor: ["#e2431e"],
            fill: false,
            borderColor: ["Red"],
            data: values,
            notuseful: percentage
          }
        ]
      };
    }
  }
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
