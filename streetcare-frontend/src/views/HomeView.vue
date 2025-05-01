<script setup>
import { ref, onMounted } from 'vue'
import { loadGoogleMaps, createMap } from '@/services/mapService'
import { getCurrentUser } from '@/services/authService'
import router from '@/router'

const postcode = ref('')
const map = ref(null)
const markers = ref([])
const showDialog = ref(false)
const clickedLocation = ref(null)

//Get potholes in the area using the provided postcode
const getPotholes = async (postcode) => {
  try {
    const response = await fetch(`http://localhost:8080/api/pothole?postcode=${postcode}`)
    if (!response.ok) throw new Error('Failed to fetch potholes')
    const potholes = await response.json()

    //
    markers.value.forEach((marker) => marker.setMap(null))
    markers.value = []

    //Add marker on the map foe each pothole
    potholes.forEach((pothole) => {
      const marker = new window.google.maps.Marker({
        position: { lat: pothole.latitude, lng: pothole.longitude },
        map: map.value,
        title: 'Pothole Reported',
      })
      markers.value.push(marker) //Add the marker to the markers array
    })
  } catch (error) {
    console.error('Error fetching potholes:', error)
  }
}

//Find postcode area and center the map
const findPostcodeArea = () => {
  if (!postcode.value || !map.value) return

  const geocoder = new window.google.maps.Geocoder()
  geocoder.geocode({ address: postcode.value }, (results, status) => {
    if (status === 'OK') {
      //Center tand zoom the map to the geocoded postcode location
      const location = results[0].geometry.location
      map.value.setCenter(location)
      map.value.setZoom(14)
      getPotholes(postcode.value) //Get potholes in the area
    } else {
      alert('Postcode not found.')
    }
  })
}

//Handle map click event
const handleMapClick = (event) => {
  clickedLocation.value = event.latLng
  showDialog.value = true
}

//Handle the dialog confirmation
const handleYes = () => {
  const user = getCurrentUser()
  if (!user) {
    router.push('/login') //Redirect to login if user is not logged in
    return
  }
  showDialog.value = false
}

//Handle the dialog cancellation
const handleNo = () => {
  showDialog.value = false
}

//Load Google Maps and create the map
onMounted(async () => {
  const mapElement = document.getElementById('map')
  if (!mapElement) return

  await loadGoogleMaps() // Load the Google Maps API and initialize the map
  map.value = createMap(mapElement)

  window.google.maps.event.addListener(map.value, 'click', handleMapClick)
})
</script>

<template>
  <!-- Welcome to StreetCare -->
  <div class="map-container">
    <h1>Welcome to StreetCare</h1>

    <!-- Map instructions -->
    <p class="map-instruction">
      To see potholes in your area, please enter your postcode. You can explore the map, and if
      you'd like to report a pothole, just click on the map.
    </p>

    <!-- Postcode input form  -->
    <form @submit.prevent="findPostcodeArea" class="postcode-form">
      <input v-model="postcode" placeholder="Enter postcode" required />
      <button type="submit">Search</button>
    </form>

    <!-- Map page -->
    <div id="map" class="map-area"></div>

    <!-- Pop up window for reporting pothole -->
    <div v-if="showDialog" class="dialog-overlay">
      <div class="dialog-box">
        <p>Do you want to report a pothole at this location?</p>
        <button @click="handleYes">Yes</button>
        <button @click="handleNo">No</button>
      </div>
    </div>
  </div>
</template>

<style>
.map-container {
  max-width: 600px;
  margin: 3em auto;
  padding: 2em;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.map-container h1 {
  text-align: center;
  margin-bottom: 1em;
}

.map-instruction {
  margin-bottom: 1.5em;
  font-size: 1rem;
  color: #444;
  text-align: center;
}

.postcode-form {
  display: flex;
  gap: 0.5em;
  margin-bottom: 1.5em;
  justify-content: center;
}

.postcode-form input {
  padding: 0.75em;
  border: 1px solid #ccc;
  border-radius: 5px;
  flex: 1;
  max-width: 300px;
}

.postcode-form input::placeholder {
  color: #888;
}

.postcode-form button {
  padding: 0.75em 1.25em;
  background-color: #2f855a;
  color: white;
  border: none;
  border-radius: 5px;
  font-weight: bold;
  cursor: pointer;
}

.postcode-form button:hover {
  background-color: #276749;
}

.map-area {
  height: 500px;
  border-radius: 10px;
  overflow: hidden;
  margin-top: 1em;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
}

.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.dialog-box {
  background-color: white;
  padding: 1.5em;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
  text-align: center;
}

.dialog-box button {
  margin: 0 1em;
  padding: 0.5em 1em;
}
</style>
