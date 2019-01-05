<template>
  <div class="graph" v-loading="loading">
    <div class="text graph-title">Influencias entre Deportes y Usuarios</div>
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
          this.fillData(response.data.nodes, response.data.links);
        })
        .catch(error => {
          console.log(error);
        })
        .finally(() => {
          this.loading = false;
        });
    },
    fillData(nodes, links) {
      let i = 0;

      _.each(nodes, node => {
        if (node.label == 'Deporte') {
          this.nodes.push({
            id: i,
            name: node.name,
            _color: 'orange',
            _size: 20,
          });
        } else {
          this.nodes.push({
            id: i,
            name: node.name,
            _color: '#00aaff',
            _size: 50,
          });
        }
        i++;
      });

      _.each(links, link => {
        this.links.push({
          sid: link.sid,
          tid: link.tid,
          _color: '#333333',
        });
      });

      this.options = {
        force: 3000,
        size: { w: 1200, h: 480 },
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

.graph {
}
</style>
