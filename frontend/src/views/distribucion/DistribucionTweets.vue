<template>
  <div>
    <div class="map" v-loading="loading">
      <div class="text">Cantidad de tweets por país</div>
      <div class="chartdiv" ref="chartdiv"></div>
    </div>
    <el-col :span="7" class="map-details-container">
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
          Tweets por millones de persona: <b> {{ details.index }} </b>
        </div>
        <div class="node-detail">
          Porcentaje de Tweets: <b> {{ details.percent }} %</b>
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
        title: '',
        index: 0,
        percent: 0,
      },
      total: 1,
      detailsVisible: false,
      minColor: '#f7fcb9',
      maxColor: '#004529',
    };
  },
  mounted() {
    this.getCountries();
  },
  methods: {
    setChart() {
      // set Chart
      let chart = am4core.create(this.$refs.chartdiv, am4maps.MapChart);

      //test
      _.each(['AR', 'CL', 'CO', 'EC', 'ES', 'MX', 'PY', 'UY', 'VE'], code => {
        let index = _.findIndex(am4geodata_worldLow.default.features, data => {
          return data.id == code;
        });
        if (index >= 0) {
          let country = am4geodata_worldLow.default.features[index];
          am4geodata_worldLow.default.features.splice(index, 1);
          country.properties.value =
            (
              parseFloat(
                _.find(this.countries, c => {
                  return c.countryCode == country.id;
                }).index
              ) / this.total
            ).toFixed(4) * 100;
          am4geodata_worldLow.default.features.push(country);
        }
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

      this.chart = chart;
    },
    handleCountry(ev) {
      _.each(this.chart.series.values[0].children.values, m => {
        if (m.className == 'MapPolygon') {
          m.isActive = false;
        }
      });
      ev.target.isActive = !ev.target.isActive;
      this.details.title = ev.target.dataItem._dataContext.name;
      this.details.index = (
        (ev.target.dataItem._dataContext.value / 100.0) *
        this.total
      ).toFixed(2);
      this.details.percent = ev.target.dataItem._dataContext.value.toFixed(2);
      this.detailsVisible = true;
    },
    closeDetails() {
      _.each(this.chart.series.values[0].children.values, m => {
        if (m.className == 'MapPolygon') {
          m.isActive = false;
        }
      });
      this.detailsVisible = false;
    },
    getCountries() {
      let self = this;
      PaisesResources.get({})
        .then(response => {
          self.countries = response.data;
          self.total = _.reduce(
            self.countries,
            (sum, c) => {
              return parseFloat(sum) + parseFloat(c.index);
            },
            0
          );
          self.setChart();
        })
        .catch(error => {
          console.log(error);
        })
        .finally(() => {
          this.loading = false;
        });
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
  margin-top: -22%;
  z-index: 1;
  position: relative;
}
</style>
