<template>
  <div class="map" v-loading="loading">
    <div class="text">Cantidad de tweets por país</div>
    <div class="chartdiv" ref="chartdiv"></div>

    <el-col :span="6" class="map-details-container">
      <el-card class="box-card" v-show="detailsVisible">
        <div slot="header" class="clearfix">
          <span> {{ details.title }} </span>
          <el-button
            style="float: right; padding: 3px 0"
            @click="closeDetails"
            type="text"
          >
            <i class="el-icon-close"></i>
          </el-button>
        </div>
        <div class="node-detail">
          Tweets por persona: <b> {{ details.index }} </b>
        </div>
        <div class="node-detail">
          Porcentaje: <b> {{ details.percent }} %</b>
        </div>
      </el-card>
    </el-col>
  </div>
</template>

<script>
import * as am4core from '@amcharts/amcharts4/core';
import * as am4maps from '@amcharts/amcharts4/maps';
import { PaisesResources } from './../../router/endpoints';
import am4themes_animated from '@amcharts/amcharts4/themes/animated';
import * as am4geodata_worldLow from '@amcharts/amcharts4-geodata/worldLow';

am4core.useTheme(am4themes_animated);

export default {
  data() {
    return {
      chart: {},
      countries: [],
      loading: true,
      details: {
        title: 'aaaa',
        index: 0,
        percent: 0,
      },
      detailsVisible: false,
      minColor: '#f7fcb9',
      maxColor: '#004529',
    };
  },
  mounted() {
    console.log('aaaa');
    this.setChart();
    this.getCountries();
  },
  methods: {
    setChart() {
      // set Chart
      let chart = am4core.create(this.$refs.chartdiv, am4maps.MapChart);
      console.log('chart', chart);

      //test
      let value = 5;
      _.each(['AR', 'EC', 'CO', 'UY', 'PY', 'ES', 'MX', 'CL', 'VE'], code => {
        let index = _.findIndex(am4geodata_worldLow.default.features, data => {
          return data.id == code;
        });
        let country = am4geodata_worldLow.default.features[index];
        am4geodata_worldLow.default.features.splice(index, 1);
        country.properties.value = value;
        am4geodata_worldLow.default.features.push(country);

        value = value + 10;
      });

      chart.geodata = am4geodata_worldLow.default;

      // Series for World map
      var worldSeries = chart.series.push(new am4maps.MapPolygonSeries());
      worldSeries.include = [
        'EC',
        'CO',
        'UY',
        'AR',
        'PY',
        'ES',
        'MX',
        'CL',
        'VE',
      ];
      worldSeries.useGeodata = true;

      worldSeries.heatRules.push({
        property: 'fill',
        target: worldSeries.mapPolygons.template,
        min: am4core.color(this.minColor),
        max: am4core.color(this.maxColor),
      });

      var polygonTemplate = worldSeries.mapPolygons.template;
      polygonTemplate.tooltipText = '{name}: {value} %';
      // polygonTemplate.fill = chart.colors.getIndex(0);
      // Hover state
      var hs = polygonTemplate.states.create('hover');
      hs.properties.fill = chart.colors.getIndex(0).brighten(-0.5);
      // hs.properties.fill = am4core.color('#367B25');

      // Create active state
      var activeState = polygonTemplate.states.create('active');
      activeState.properties.fill = chart.colors.getIndex(3).brighten(-0.5);
      polygonTemplate.events.on('hit', this.handleCountry);
      console.log('aaaaaa', polygonTemplate.events.target);

      // heat Legend
      var heatLegend = chart.createChild(am4maps.HeatLegend);
      heatLegend.series = worldSeries;
      heatLegend.width = am4core.percent(100);

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
        'BE',
        'CH',
        'DE',
        'NL',
        'PT',
        'FR',
        'IT',
        'GB',
        'IE',
      ];
      worldSeries.useGeodata = true;
      polygonTemplate.fill = chart.colors.getIndex(1);

      // zoom control
      chart.zoomControl = new am4maps.ZoomControl();
      chart.zoomControl.slider.height = 100;
      chart.projection = new am4maps.projections.Miller();

      //test
      this.chart = chart;
      this.loading = false;
      console.log('chart', this.chart);
    },
    handleCountry(ev) {
      ev.target.isActive = !ev.target.isActive;
      this.details.title = ev.target.dataItem._dataContext.name;
      this.details.index = ev.target.dataItem._dataContext.value;
      // todo change to percent
      this.details.percent = ev.target.dataItem._dataContext.value;
      console.log('bbbb', ev.target);
      this.detailsVisible = true;
    },
    closeDetails() {
      this.detailsVisible = false;
    },
    getCountries() {
      let self = this;
      PaisesResources.get({})
        .then(response => {
          self.countries = response.data;
          console.log(self.countries);
          // self.fillData();
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
  min-width: 800px;
  height: 550px;
  z-index: 0;
  position: relative;
}

.map {
  margin-top: 20px;
  margin-bottom: 20px;
  max-width: 800px;
  margin-left: auto;
  margin-right: auto;
}

.map-details-container {
  margin-top: -180px;
  z-index: 1;
  position: relative;
}
</style>
