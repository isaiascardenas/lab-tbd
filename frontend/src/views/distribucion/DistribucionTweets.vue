<template>
  <div class="small" v-loading="loading">
    <div class="text">Cantidad de tweets por país</div>
    <div class="chartdiv" ref="chartdiv"></div>
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
    };
  },
  mounted() {
    this.setChart();
    // this.getDeportes();
  },
  methods: {
    setChart() {
      // set Chart
      let chart = am4core.create(this.$refs.chartdiv, am4maps.MapChart);
      chart.geodata = am4geodata_worldLow.default;
      chart.projection = new am4maps.projections.Miller();

      // Series for World map
      var worldSeries = chart.series.push(new am4maps.MapPolygonSeries());
      worldSeries.include = [
        'AR',
        'EC',
        'CO',
        'UY',
        'PY',
        'ES',
        'MX',
        'CL',
        'VE',
      ];
      worldSeries.useGeodata = true;

      var polygonTemplate = worldSeries.mapPolygons.template;
      polygonTemplate.tooltipText = '{name}';
      polygonTemplate.fill = chart.colors.getIndex(0);
      // Hover state
      var hs = polygonTemplate.states.create('hover');
      hs.properties.fill = am4core.color('#367B25');

      // Other countries
      worldSeries = chart.series.push(new am4maps.MapPolygonSeries());
      worldSeries.include = [
        'BR',
        'BO',
        'GY',
        'PE',
        'TT',
        'SR',
        'AW',
        'GF',
        'FK',
        'GS',
        'PA',
        'CR',
        'NI',
        'HN',
        'SV',
        'GT',
        // 'US',
        // 'CA',
        'PT',
        'FR',
        'IT',
        'GB',
        'IE',
      ];
      worldSeries.useGeodata = true;

      polygonTemplate.fill = chart.colors.getIndex(1);

      //test
      this.chart = chart;
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
    fillData() {},
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
