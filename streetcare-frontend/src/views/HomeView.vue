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

const fetchPotholes = async (postcode) => {
  try {
    const response = await fetch(`http://localhost:8080/api/pothole?postcode=${postcode}`)
    if (!response.ok) throw new Error('Failed to fetch potholes')
    const potholes = await response.json()

    markers.value.forEach((marker) => marker.setMap(null))
    markers.value = []

    potholes.forEach((pothole) => {
      const marker = new window.google.maps.Marker({
        position: { lat: pothole.latitude, lng: pothole.longitude },
        map: map.value,
        title: 'Pothole Reported',
      })
      markers.value.push(marker)
    })
  } catch (error) {
    console.error('Error fetching potholes:', error)
  }
}

const findPostcodeArea = () => {
  if (!postcode.value || !map.value) return

  const geocoder = new window.google.maps.Geocoder()
  geocoder.geocode({ address: postcode.value }, (results, status) => {
    if (status === 'OK') {
      const location = results[0].geometry.location
      map.value.setCenter(location)
      map.value.setZoom(14)
      fetchPotholes(postcode.value)
    } else {
      alert('Postcode not found.')
    }
  })
}

const handleMapClick = (event) => {
  clickedLocation.value = event.latLng
  showDialog.value = true
}

const handleYes = () => {
  const user = getCurrentUser()
  if (!user) {
    router.push('/login')
  }
  showDialog.value = false
}

const handleNo = () => {
  showDialog.value = false
}

onMounted(async () => {
  const mapElement = document.getElementById('map')
  if (!mapElement) return

  await loadGoogleMaps()
  map.value = createMap(mapElement)

  window.google.maps.event.addListener(map.value, 'click', handleMapClick)
})
</script>

<template>
  <div class="map-container">
    <h1>Welcome to the Pothole Map</h1>

    <p class="map-instruction">
      To see potholes in your area, please enter your postcode. You can explore the map, and if
      you'd like to report a pothole, just click on the map.
    </p>

    <form @submit.prevent="findPostcodeArea" class="postcode-form">
      <input v-model="postcode" placeholder="Enter postcode" required />
      <button type="submit">Search</button>
    </form>

    <div id="map" class="map-area"></div>

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

<!-- <style>
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
</style> -->
