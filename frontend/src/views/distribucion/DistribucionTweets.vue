<template>
  <div class="small" v-loading="loading">
    <div class="text">Cantidad de tweets por deportes</div>
    <div class="chartdiv" ref="chartdiv">Cantidad de tweets por deportes</div>
  </div>
</template>

<script>
import * as am4core from '@amcharts/amcharts4/core';
import * as am4maps from '@amcharts/amcharts4/maps';
import { DeportesResources } from './../../router/endpoints';
import am4themes_animated from '@amcharts/amcharts4/themes/animated';
import * as am4geodata_usaLow from '@amcharts/amcharts4-geodata/usaLow';
import * as am4geodata_worldLow from '@amcharts/amcharts4-geodata/worldLow';

am4core.useTheme(am4themes_animated);

export default {
  data() {
    return {
      chart: {},
      deportes: [],
      loading: true,
      opciones: {
        tooltips: {
          callbacks: {
            title: function(tooltipItem, data) {
              return data['labels'][tooltipItem[0]['index']];
            },
            label: function(tooltipItem, data) {
              return (
                'Total: ' +
                data['datasets'][0]['data'][tooltipItem['index']] +
                ' (' +
                data['datasets'][0]['notuseful'][tooltipItem['index']] +
                '%)'
              );
            },
          },
          backgroundColor: '#000',
          titleFontSize: 16,
          titleFontColor: '#FFF',
          bodyFontColor: '#FFF',
          bodyFontSize: 14,
          displayColors: true,
        },
        legend: {
          display: false,
        },
      },
    };
  },
  mounted() {
    this.setChart();
    // this.getDeportes();
  },
  methods: {
    setChart() {
      let chart = am4core.create(this.$refs.chartdiv, am4maps.MapChart);

      // Set map definition
      chart.geodata = am4geodata_worldLow;

      // Set projection
      chart.projection = new am4maps.projections.Miller();

      // Series for World map
      var worldSeries = chart.series.push(new am4maps.MapPolygonSeries());
      worldSeries.exclude = ['AQ'];
      worldSeries.useGeodata = true;

      var polygonTemplate = worldSeries.mapPolygons.template;
      polygonTemplate.tooltipText = '{name}';
      polygonTemplate.fill = chart.colors.getIndex(0);

      // Hover state
      var hs = polygonTemplate.states.create('hover');
      hs.properties.fill = am4core.color('#367B25');

      // Series for United States map
      var usaSeries = chart.series.push(new am4maps.MapPolygonSeries());
      usaSeries.geodata = am4geodata_usaLow;

      var polygonTemplate = usaSeries.mapPolygons.template;
      polygonTemplate.tooltipText = '{name}';
      polygonTemplate.fill = chart.colors.getIndex(1);

      // Hover state
      var hs = polygonTemplate.states.create('hover');
      hs.properties.fill = am4core.color('#367B25');

      this.chart = chart;
      //test
      this.loading = false;
    },
    getDeportes() {
      let self = this;
      DeportesResources.get({})
        .then(response => {
          self.deportes = response.data;
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
      let labels = _.map(this.deportes, sport => {
        return sport.sportName;
      });
      let values = _.map(this.deportes, sport => {
        return sport.sportTweetCount;
      });
      let total = _.reduce(values, function(sum, n) {
        return sum + n;
      });
      let percentage = _.transform(values, function(result, n) {
        result.push(Math.round(((n * 100) / total) * 10) / 10);
        return result;
      });
      this.datacollection = {
        labels: [
          'Rugby',
          'Basketball',
          'Tenis',
          'Boxeo',
          'Volleyball',
          'Natación',
          'Futbol femenino',
        ],
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
            notuseful: percentage,
          },
        ],
      };
    },
    beforeDestroy() {
      if (this.chart) {
        this.chart.dispose();
      }
    },
  },
};
</script>

<style>
.chartdiv {
  width: 100%;
  height: 550px;
}

.small {
  margin-top: 20px;
  margin-bottom: 20px;
  max-width: 460px;
  margin-left: auto;
  margin-right: auto;
}
</style>
