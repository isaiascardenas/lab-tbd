<template>
  <div class="small" v-loading="loading">
    <div class="text graph-title">Cantidad de tweets por deportes</div>
    <d3-network :net-nodes="nodes" :net-links="links" :options="options" />
  </div>
</template>

<script>
import D3Network from 'vue-d3-network';
import { DeportesResources } from './../../router/endpoints';

export default {
  components: {
    D3Network,
  },
  data() {
    return {
      nodes: [],
      options: {},
      links: [],
      deportes: [],
      loading: true,
    };
  },
  mounted() {
    this.fillData();
    this.loading = false;
    // this.getDeportes();
  },
  methods: {
    getDeportes() {
      return;
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
      this.nodes = [
        { id: 1, name: 'my awesome node 1' },
        { id: 2, name: 'my node 2' },
        { id: 3, name: 'orange node', _color: 'orange' },
        { id: 4, name: 'blue node', _color: '#00aaff' },
        { id: 5, name: 'blue node', _color: '#00aaff', _size: 30 },
        { id: 6 },
        { id: 7 },
        { id: 8 },
        { id: 9 },
        { id: 10 },
        { id: 11 },
        { id: 12 },
        { id: 13 },
        { id: 14 },
      ];
      this.links = [
        { sid: 1, tid: 2, _color: '#333333' },
        { sid: 2, tid: 8 },
        { sid: 4, tid: 3 },
        { sid: 4, tid: 5 },
        { sid: 4, tid: 9 },
        { sid: 5, tid: 6 },
        { sid: 7, tid: 8 },
        { sid: 5, tid: 8 },
        { sid: 3, tid: 8 },
        { sid: 7, tid: 9 },
        { sid: 3, tid: 9 },
        { sid: 10, tid: 6 },
        { sid: 11, tid: 6 },
        { sid: 12, tid: 6 },
        { sid: 13, tid: 6 },
        { sid: 14, tid: 6 },
      ];
      this.options = {
        force: 3000,
        size: { w: 500, h: 400 },
        nodeSize: 10,
        nodeLabels: true,
        canvas: false,
        linkWidth: 1,
        strLinks: true,
      };
    },
  },
};
</script>

<style>
.graph-title {
  margin-bottom: 20px;
}

.small {
  margin-top: 20px;
  margin-bottom: 20px;
  max-width: 460px;
  margin-left: auto;
  margin-right: auto;
}
</style>
