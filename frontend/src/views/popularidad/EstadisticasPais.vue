<template>
  <div class="small" v-loading="loading">
    <div class="text">Cantidad de tweets sobre deportes en {{ paisName }}</div>
    <bar-chart :chart-data="datacollection" :options="opciones"></bar-chart>
  </div>
</template>

<script>
import BarChart from "./../../charts/HorizontalBarChart.js";
import { EstadisticasResources } from "./../../router/endpoints";

export default {
  components: {
    BarChart
  },
  data() {
    return {
      datacollection: {},
      deportes: [],
      paisName: "",
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
          displayColors: true
        },
        legend: {
          display: false
        }
      }
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
            label: "Deporte",
            backgroundColor: [
              "#536DFE",
              "#FFC107",
              "#CDDC39",
              "#8BC34A",
              "#607D8B",
              "#9E9E9E",
              "#00BCD4"
            ],
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
