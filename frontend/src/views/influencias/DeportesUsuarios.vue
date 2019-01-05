<template>
  <div class="small" v-loading="loading">
    <div class="text graph-title">Cantidad de tweets por deportes</div>
    <d3-network :net-nodes="nodes" :net-links="links" :options="options" />
  </div>
</template>

<script>
import D3Network from 'vue-d3-network';
import { Neo4jResources } from './../../router/endpoints';

export default {
  components: {
    D3Network,
  },
  data() {
    return {
      nodes: [],
      options: {},
      links: [],
      loading: true,
    };
  },
  mounted() {
    this.getData();
  },
  methods: {
    getData() {
      let self = this;
      Neo4jResources.getDeportesUsuarios({})
        .then(response => {
          console.log('data', response.data);
          self.nodes = response.data.nodes;
          self.links = response.data.links;
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
      let i = 0;
      _.map(this.nodes, node => {
        if (node.label == 'Deporte') {
          return {
            id: i,
            name: node.name,
            _color: 'orange',
            size: 40,
          };
        } else {
          return {
            id: i,
            name: node.name,
            _color: '#00aaff',
            size: 40,
          };
        }
      });
      this.links = [
        { sid: 1, tid: 2, _color: '#333333' },
        { sid: 2, tid: 8, _color: '#333333' },
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
