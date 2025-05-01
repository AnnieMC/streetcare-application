<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { loadGoogleMaps, createMap } from '@/services/mapService'
import { getCurrentUser } from '@/services/authService'

const router = useRouter()
const postcode = ref('')
const feedback = ref('')
const map = ref(null)
const markers = ref([])
const user = ref(null)

const logout = () => {
  localStorage.removeItem('user')
  router.push('/login')
}

const findPostcodeArea = () => {
  if (!postcode.value || !map.value) return
  const geocoder = new window.google.maps.Geocoder()

  geocoder.geocode({ address: postcode.value }, (results, status) => {
    if (status === 'OK') {
      const location = results[0].geometry.location
      map.value.setCenter(location)
      map.value.setZoom(14)
    } else {
      alert('Postcode not found.')
    }
  })
}

// Save feedback to the database
const saveFeedback = async () => {
  if (!feedback.value.trim()) {
    alert('Please enter feedback before submitting.')
    return
  }

  const potholeId =
    markers.value.length > 0 ? markers.value[markers.value.length - 1].potholeId : null

  if (!potholeId) {
    alert('No pothole marker found.')
    return
  }

  const feedbackData = {
    userId: user.value.id,
    potholeId: potholeId,
    comment: feedback.value,
  }

  try {
    const response = await fetch('http://localhost:8080/api/feedback', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(feedbackData),
    })

    if (!response.ok) {
      throw new Error('Failed to submit feedback.')
    }

    alert('Feedback submitted successfully!')
    feedback.value = '' // Clear feedback box after submission
  } catch (error) {
    console.error('Error submitting feedback:', error)
    alert('Error submitting feedback:', error.message)
  }
}

onMounted(async () => {
  user.value = getCurrentUser()
  if (!user.value) {
    alert('Please log in first.')
    return router.push('/login')
  }

  await loadGoogleMaps()
  console.log('Google Maps loaded')

  const mapElement = document.getElementById('map')
  map.value = createMap(mapElement)

  map.value.addListener('click', async (event) => {
    if (!event.latLng) return

    const marker = new window.google.maps.Marker({
      position: event.latLng,
      map: map.value,
      animation: window.google.maps.Animation.DROP,
    })

    markers.value.push(marker)

    const lat = event.latLng.lat()
    const lng = event.latLng.lng()

    const potholeData = {
      userId: user.value.id,
      latitude: lat,
      longitude: lng,
    }

    // Step 1: Create pothole
    const potholeResponse = await fetch('http://localhost:8080/api/pothole', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(potholeData),
    })

    if (!potholeResponse.ok) {
      alert('Failed to report pothole')
      return
    }

    const potholeResult = await potholeResponse.json()
    marker.potholeId = potholeResult.id // Store potholeId on the marker
  })
})
</script>

<template>
  <div class="welcome-container">
    <h2>Welcome, {{ user?.name }}</h2>

    <form @submit.prevent="findPostcodeArea" class="search-form">
      <div class="form-group">
        <input v-model="postcode" placeholder="Enter postcode" class="input-field" />
      </div>

      <button type="submit" class="submit-button">Search</button>
    </form>

    <div class="form-group">
      <textarea v-model="feedback" placeholder="Leave feedback" class="textarea-field"></textarea>
    </div>

    <button @click="saveFeedback" class="submit-button">Submit Feedback</button>

    <div id="map" class="map-container"></div>

    <button @click="logout" class="logout-button">Logout</button>
  </div>
</template>

<style>
.welcome-container {
  max-width: 600px;
  margin: 2em auto;
  padding: 2em;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  text-align: left;
}

.welcome-container h2 {
  text-align: center;
  margin-bottom: 1.5em;
}

.search-form {
  margin-bottom: 1.5em;
}

.form-group {
  margin-bottom: 1.5em;
}

.input-field,
.textarea-field {
  width: 100%;
  padding: 0.75em;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 1em;
}

.input-field::placeholder,
.textarea-field::placeholder {
  color: #888;
}

.submit-button,
.logout-button {
  width: 100%;
  padding: 0.75em;
  background-color: #2f855a;
  color: white;
  border: none;
  border-radius: 5px;
  font-weight: bold;
  cursor: pointer;
  margin-bottom: 1em;
}

.submit-button:hover,
.logout-button:hover {
  background-color: #276749;
}

.map-container {
  height: 500px;
  margin-bottom: 1.5em;
}

.logout-button {
  background-color: #e53e3e;
}

.logout-button:hover {
  background-color: #c53030;
}
</style>
