<template>
  <div>
    <div id="map" class="container"></div>
    <!-- <div class="button-group">
      <button @click="changeSize(0)">Hide</button>
      <button @click="changeSize(400)">show</button>
      <button @click="displayMarker(markerPositions1)">marker set 1</button>
      <button @click="displayMarker(markerPositions2)">marker set 2</button>
      <button @click="displayMarker([])">marker set 3 (empty)</button>
      <button @click="displayInfoWindow">infowindow</button>
    </div> -->
  </div>
</template>

<script>
export default {
  name: "KakaoMap",
  computed: {
    attractionList(){
            return this.$store.getters.getTempAttractionList;
        }
  },
  watch: {
    attractionList(){
        this.markerPositions = [];
        
        const attList = this.attractionList;

        for (let index = 0; index < attList.length; index++) { 
            this.markerPositions.push([attList[index].latitude, attList[index].longitude]);
        }

        if (this.markers.length > 0) {
        this.markers.forEach((marker) => marker.setMap(null));
      }

      if(this.lines != null) {
        this.lines.setMap(null);
      }

      const positions = this.markerPositions.map(
        (position) => new kakao.maps.LatLng(...position)
      );

      if (positions.length > 0) {
        let num = 1;
        this.markers = positions.map(
          (position) =>
            new kakao.maps.CustomOverlay({
              map: this.map,
              position,
              content: `<button type="button" class="btn btn-dark">${num++}</button>`
            })
        );

        this.lines =  
            new kakao.maps.Polyline({
                map: this.map,
                path: positions,
                strokeWeight: 3, // 선의 두께입니다 
            strokeColor: '#db4040', // 선의 색깔입니다
            strokeOpacity: 1, // 선의 불투명도입니다 0에서 1 사이값이며 0에 가까울수록 투명합니다
            strokeStyle: 'solid' // 선의 스타일입니다
            })

        const bounds = positions.reduce(
          (bounds, latlng) => bounds.extend(latlng),
          new kakao.maps.LatLngBounds()
        );

        this.map.setBounds(bounds);
      }
    }
  }
  ,
  data() {
    return {
      markerPositions: [
      ],
      markers: [],
      lines: null,
      infowindow: null,
    };
  },
  mounted() {    
    if (window.kakao && window.kakao.maps) {
      this.initMap();
    } else {
      const script = document.createElement("script");
      /* global kakao */
      script.onload = () => kakao.maps.load(this.initMap);
      script.src =
        "//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=01804873cc789c708a3675143bc91919";
      document.head.appendChild(script);
    }
  },
  methods: {
    initMap() {
      const container = document.getElementById("map");
      const options = {
        center: new kakao.maps.LatLng(33.450701, 126.570667),
        level: 5,
      };

      //지도 객체를 등록합니다.
      //지도 객체는 반응형 관리 대상이 아니므로 initMap에서 선언합니다.
      this.map = new kakao.maps.Map(container, options);
    },
    // changeSize(size) {
    //   const container = document.getElementById("map");
    //   container.style.width = `${size}px`;
    //   container.style.height = `${size}px`;
    //   this.map.relayout();
    // },
    // displayMarker(markerPositions) {
    //   if (this.markers.length > 0) {
    //     this.markers.forEach((marker) => marker.setMap(null));
    //   }

    //   const positions = markerPositions.map(
    //     (position) => new kakao.maps.LatLng(...position)
    //   );

    //   if (positions.length > 0) {
    //     this.markers = positions.map(
    //       (position) =>
    //         new kakao.maps.Marker({
    //           map: this.map,
    //           position,
    //         })
    //     );

    //     const bounds = positions.reduce(
    //       (bounds, latlng) => bounds.extend(latlng),
    //       new kakao.maps.LatLngBounds()
    //     );

    //     this.map.setBounds(bounds);
    //   }
    // },
    displayInfoWindow() {
      if (this.infowindow && this.infowindow.getMap()) {
        //이미 생성한 인포윈도우가 있기 때문에 지도 중심좌표를 인포윈도우 좌표로 이동시킨다.
        this.map.setCenter(this.infowindow.getPosition());
        return;
      }

      var iwContent = '<div style="padding:5px;">Hello World!</div>', // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
        iwPosition = new kakao.maps.LatLng(33.450701, 126.570667), //인포윈도우 표시 위치입니다
        iwRemoveable = true; // removeable 속성을 ture 로 설정하면 인포윈도우를 닫을 수 있는 x버튼이 표시됩니다

      this.infowindow = new kakao.maps.InfoWindow({
        map: this.map, // 인포윈도우가 표시될 지도
        position: iwPosition,
        content: iwContent,
        removable: iwRemoveable,
      });

      this.map.setCenter(iwPosition);
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
#map {
  width: 100%;
  height: 100vh;
}

.button-group {
  margin: 10px 0px;
}

button {
  margin: 0 3px;
}
</style>
