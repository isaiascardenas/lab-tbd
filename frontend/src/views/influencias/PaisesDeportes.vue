<template>
  <div class="small" v-loading="loading">
    <div class="text graph-title">Influencias entre Paises y Deportes</div>
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
      Neo4jResources.getPaisesDeportes({})
        .then(response => {
          console.log('data', response.data);
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
        size: { w: 500, h: 400 },
        nodeSize: 10,
        nodeLabels: true,
        canvas: false,
        linkWidth: 1,
        strLinks: true,
      };

      console.log('node', nodes);
      console.log('links', links);
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
