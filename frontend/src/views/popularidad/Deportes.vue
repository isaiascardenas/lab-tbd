<template>
  <div class="small" v-loading="loading">
    <div class="text">Cantidad de tweets por deportes</div>
    <bar-chart :chart-data="datacollection" :options="opciones"></bar-chart>
  </div>
</template>

<script>
import BarChart from './../../charts/HorizontalBarChart.js';
import { DeportesResources } from './../../router/endpoints';

export default {
  components: {
    BarChart,
  },
  data() {
    return {
      datacollection: {},
      deportes: [],
      loading: true,
      opciones:{
          tooltips: {
              callbacks: {
                  title: function(tooltipItem, data) {
                      return data['labels'][tooltipItem[0]['index']];
                  },
                  label: function(tooltipItem, data) {
                      return 'Total: '+data['datasets'][0]['data'][tooltipItem['index']]+' ('+data['datasets'][0]['notuseful'][tooltipItem['index']]+'%)';
                  },
              },
          backgroundColor: '#000',
          titleFontSize: 16,
          titleFontColor: '#FFF',
          bodyFontColor: '#FFF',
          bodyFontSize: 14,
          displayColors: true
        },
        legend:{
          display:false
        }
      }
    }
  },
  mounted() {
    this.getDeportes();
  },
  methods: {
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
      let total= _.reduce(values,function(sum,n){
                return sum+n;
      });
      let percentage=_.transform(values,function(result,n){
          result.push(Math.round(n*100/total*10)/10);
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
            notuseful:percentage
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
