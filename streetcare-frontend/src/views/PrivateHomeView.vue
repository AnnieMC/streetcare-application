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

//Find potholes in the area
const findPostcodeArea = () => {
  if (!postcode.value || !map.value) return
  const geocoder = new window.google.maps.Geocoder()

  //Convert postcode to geographical coordinates using Google Maps API
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

  //Check if there are any markers
  const potholeId =
    markers.value.length > 0 ? markers.value[markers.value.length - 1].potholeId : null

  //If no markers are found, alert the user
  if (!potholeId) {
    alert('No pothole marker found.')
    return
  }

  //Feedback data to be sent to the server
  const feedbackData = {
    userId: user.value.id,
    potholeId: potholeId,
    comment: feedback.value,
  }

  try {
    //Send feedback data to the server
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

//Logout function
const logout = () => {
  localStorage.removeItem('user')
  router.push('/')
}

// Load Google Maps API and initialize the map when component is mounted
onMounted(async () => {
  user.value = getCurrentUser() //Get the current user from local storage
  if (!user.value) {
    alert('Please log in first.')
    return router.push('/login') //Redirect to login page if user is not logged in
  }

  //Load Google Maps
  await loadGoogleMaps()
  console.log('Google Maps loaded')

  const mapElement = document.getElementById('map')
  map.value = createMap(mapElement) //Create the map

  map.value.addListener('click', async (event) => {
    if (!event.latLng) return

    //Create a marker for the pothole
    const marker = new window.google.maps.Marker({
      position: event.latLng,
      map: map.value,
      animation: window.google.maps.Animation.DROP,
    })

    markers.value.push(marker) //Add the marker to the markers array

    const lat = event.latLng.lat()
    const lng = event.latLng.lng()

    //pothole data to be sent to the server
    const potholeData = {
      userId: user.value.id,
      latitude: lat,
      longitude: lng,
    }

    //Create pothole on the server
    const potholeResponse = await fetch('http://localhost:8080/api/pothole', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(potholeData),
    })

    if (!potholeResponse.ok) {
      alert('Failed to report pothole')
      return
    }

    //Get the potholeId from the response
    const potholeResult = await potholeResponse.json()
    marker.potholeId = potholeResult.id // Store potholeId on the marker
    alert('Pothole reported successfully!')
  })
})
</script>

<template>
  <!-- Welcome page -->

  <div class="welcome-container">
    <h2>Welcome, {{ user?.name }}</h2>

    <!-- Postcode input  -->
    <form @submit.prevent="findPostcodeArea" class="postcode-form">
      <input v-model="postcode" placeholder="Enter postcode" required />
      <button type="submit">Search</button>
    </form>

    <!-- Map page -->
    <div id="map" class="map-area"></div>

    <!-- Feedback field below the map -->
    <div class="form-group">
      <textarea v-model="feedback" placeholder="Leave feedback" class="textarea-field"></textarea>
    </div>
    <button @click="saveFeedback" class="submit-button">Submit Feedback</button>

    <!-- Logout button -->
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

/* From first page */
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

/* Map styling from first page */
.map-area {
  height: 500px;
  border-radius: 10px;
  overflow: hidden;
  margin-top: 1em;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
  margin-bottom: 1.5em;
}

.form-group {
  margin-bottom: 1.5em;
}

.textarea-field {
  width: 100%;
  padding: 0.75em;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 1em;
}

.textarea-field::placeholder {
  color: #888;
}

.submit-button {
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

.logout-button {
  width: 30%;
  padding: 0.75em;
  background-color: #2f855a;
  color: white;
  border: none;
  border-radius: 5px;
  font-weight: bold;
  cursor: pointer;
  margin-bottom: 1em;
}

.submit-button:hover {
  background-color: #276749;
}

.logout-button {
  background-color: #e53e3e;
}

.logout-button:hover {
  background-color: #c53030;
}
</style>
