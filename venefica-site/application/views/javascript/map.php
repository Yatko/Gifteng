<script langauge="javascript">
    function init_map(mapId, longitudeId, latitudeId, markerLongitudeId, markerLatitudeId, canDrag) {
        try {
            
            //alert('init_map: ' + mapId);
            
            if ( $('#' + mapId).length === 0 ) {
                return;
            }

            var locationIcon = L.icon({
                iconUrl: '<?=BASE_PATH?>temp-sample/ge-location-pin-teal.png',
                iconSize: [64, 64]
            });

            var marker_longitude = $("#" + markerLongitudeId).val();
            var marker_latitude = $("#" + markerLatitudeId).val();

            var locationMarker = L.marker([marker_latitude, marker_longitude], {
                icon: locationIcon,
                draggable: canDrag,
                title: (canDrag ? 'Drag me to the exact location' : ''),
            });
            if ( canDrag ) {
                locationMarker.on('dragend', function() {
                    var latlng = locationMarker.getLatLng();
                    var lng = latlng.lng;
                    var lat = latlng.lat;

                    $("#" + longitudeId).val(lng);
                    $("#" + latitudeId).val(lat);
                });
            }

            var tileLayer = L.tileLayer.provider('Esri.WorldStreetMap'); //very simple and useful
            //var tileLayer = L.tileLayer.provider('Esri.WorldImagery'); //similar with google terrain maps
            //var tileLayer = L.tileLayer.provider('Nokia.normalDay');//so-so
            //var tileLayer = L.tileLayer.provider('Nokia.satelliteYesLabelsDay'); //probably the best: very-very similar with google terrain also with labels

            /**
            var tileLayer = L.tileLayer('http://{s}.tile.cloudmade.com/c82af3890f2f47afb15da8e99832543b/997/256/{z}/{x}/{y}.png', {
                attribution: 'Gifteng',
                maxZoom: 18
            });
            /**/

            var map = L.map(mapId).setView([marker_latitude, marker_longitude], 16);
            tileLayer.addTo(map);
            locationMarker.addTo(map);
            
        } catch ( ex ) {
            //ignoring exception at 2nd init
        }
    }
</script>
