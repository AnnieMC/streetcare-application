<!-- <script setup>
import { onMounted } from 'vue'
import { loadGoogleMaps, createMap } from '@/services/mapService'
import { getCurrentUser } from '@/services/authService'
import router from '@/router'

onMounted(async () => {
  const mapElement = document.getElementById('map')
  if (!mapElement) return

  await loadGoogleMaps()
  const map = createMap(mapElement)

  map.addListener('click', () => {
    const user = getCurrentUser()
    console.log('User from getCurrentUser:', user)

    if (!user) {
      alert('Please log in first. Redirecting...')
      router.push('/login')
      return
    }
  })
})
</script>

<template>
  <div>
    <h1>Welcome to the Pothole Map</h1>
    <div id="map" style="height: 500px"></div>
  </div>
</template> -->

<script setup>
import { ref, onMounted } from 'vue'
import { loadGoogleMaps, createMap } from '@/services/mapService'
import { getCurrentUser } from '@/services/authService'
import router from '@/router'

// Define reactive references
const postcode = ref('')
const map = ref(null)
const markers = ref([]) // Store markers for potholes

// Fetch potholes based on postcode
const fetchPotholes = async (postcode) => {
  try {
    const response = await fetch(`http://localhost:8080/api/pothole?postcode=${postcode}`)
    if (!response.ok) {
      throw new Error('Failed to fetch potholes')
    }
    const potholes = await response.json()

    // Clear previous markers
    markers.value.forEach((marker) => marker.setMap(null))
    markers.value = []

    // Add a marker for each pothole on the map
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

// Handle postcode submission and update map
const findPostcodeArea = async () => {
  if (!postcode.value || !map.value) return

  const geocoder = new window.google.maps.Geocoder()

  geocoder.geocode({ address: postcode.value }, (results, status) => {
    if (status === 'OK') {
      const location = results[0].geometry.location
      map.value.setCenter(location)
      map.value.setZoom(14)

      // Fetch potholes after updating map center
      fetchPotholes(postcode.value)
    } else {
      alert('Postcode not found.')
    }
  })
}

// Handle click event to report pothole
const handleMapClick = (event) => {
  const user = getCurrentUser()
  console.log('User from getCurrentUser:', user)

  if (!user) {
    alert('Please log in first. Redirecting...')
    router.push('/login')
    return
  }

  // If logged in, allow them to report a pothole at the clicked location
  const potholeLocation = event.latLng
  alert(
    `Pothole reported at latitude: ${potholeLocation.lat()}, longitude: ${potholeLocation.lng()}`,
  )
  // Logic to save the pothole report for the logged-in user can go here
}

onMounted(async () => {
  const mapElement = document.getElementById('map')
  if (!mapElement) return

  await loadGoogleMaps()
  map.value = createMap(mapElement)

  // Add the click event listener to the map to report potholes
  window.google.maps.event.addListener(map.value, 'click', handleMapClick)
})
</script>

<template>
  <div>
    <h1>Welcome to the Pothole Map</h1>

    <!-- Postcode input and submit button -->
    <form @submit.prevent="findPostcodeArea">
      <input v-model="postcode" placeholder="Enter postcode" />
      <button>Search</button>
    </form>

    <div id="map" style="height: 500px"></div>
  </div>
</template>
