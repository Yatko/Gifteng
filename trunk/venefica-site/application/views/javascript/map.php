<script langauge="javascript">
    function init_map(canDrag) {
        if ( $('#map').length === 0 ) {
            return;
        }
        
        //#map element exists
        var locationIcon = L.icon({
            iconUrl: '<?=BASE_PATH?>temp-sample/ge-location-pin-teal.png',
            iconSize: [64, 64]
        });
        
        var marker_longitude = $("#marker_longitude").val();
        var marker_latitude = $("#marker_latitude").val();
        
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

                $("#longitude").val(lng);
                $("#latitude").val(lat);
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

        var map = L.map('map').setView([marker_latitude, marker_longitude], 16);
        tileLayer.addTo(map);
        locationMarker.addTo(map);
    }
</script>
