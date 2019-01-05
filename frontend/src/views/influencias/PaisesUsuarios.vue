<template>
  <div class="graph" v-loading="loading">
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
      Neo4jResources.getPaisesUsuarios({})
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
        if (node.label == 'Pais') {
          this.nodes.push({
            id: i,
            name: node.name,
            _color: '#00aaff',
            _size: this.setCountryNodeSize(node.influencia),
          });
        } else {
          this.nodes.push({
            id: i,
            name: node.name,
            _color: '#00897b',
            _size: this.setUserNodeSize(node.influencia),
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
    setUserNodeSize(influencia) {
      if (influencia > 60000000) {
        return 80;
      } else if (influencia < 4000000) {
        return 30;
      }

      return Math.floor((50 / 56000000) * influencia + 30);
    },
    setCountryNodeSize(influencia) {
      if (influencia > 90000) {
        return 80;
      } else if (influencia < 6000) {
        return 30;
      }

      return Math.floor((50 / 64000) * influencia + 30);
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
