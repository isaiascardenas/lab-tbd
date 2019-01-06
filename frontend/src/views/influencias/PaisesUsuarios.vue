<template>
  <div class="graph" v-loading="loading">
    <div class="text graph-title">Influencias entre Paises y Usuarios</div>
    <d3-network
      :net-nodes="nodes"
      :net-links="links"
      :options="options"
      @node-click="showNode"
    />

    <el-col :span="6" class="details-container">
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
        <div class="node-detail">{{ details.type }}</div>
        <div class="node-detail">
          Índice de influencias: <b> {{ details.influencia }} </b>
        </div>
        <div class="node-detail">
          Porcentaje de influencias: <b> {{ details.porcentaje }} %</b>
        </div>
      </el-card>
    </el-col>
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
      details: {
        title: '',
        type: '',
        influencia: 0,
        porcentaje: 0,
      },
      detailsVisible: false,
      loading: true,
    };
  },
  mounted() {
    this.getData();
  },
  methods: {
    showNode(event, node) {
      this.details.type = 'Usuario';
      this.details.title = node.name;
      this.details.influencia = this.setNodeInfluence(node._size);
      this.details.porcentaje = 15; // make formula to calculate this
      if (node._color == '#00aaff') {
        this.details.type = 'Pais';
      }
      this.detailsVisible = true;
    },
    closeDetails() {
      this.detailsVisible = false;
    },
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
      if (influencia > 3000) {
        return 80;
      } else if (influencia < 100) {
        return 30;
      }

      return Math.floor((50 / 2900) * influencia + 30);
    },
    setNodeInfluence(size) {
      return ((size - 30) * 56000000) / 50; // error, 0 if 30
    },
  },
};
</script>

<style>
.graph-title {
  margin-bottom: 20px;
}

.node-label {
  font-size: 15px;
}

.net {
  z-index: 0;
  position: relative;
}

.details-container {
  margin-top: -180px;
  z-index: 1;
  position: relative;
}
</style>
