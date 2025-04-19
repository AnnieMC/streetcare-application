<script>
import { ref, onMounted } from 'vue'
import { fetchUsers, getCurrentUser } from '@/services/authService'
import { createMap, loadGoogleMaps } from '@/services/mapService'
import router from '@/router'

export default {
  setup() {
    const users = ref([])
    const mapError = ref(null)
    const markers = ref([])
    const userInput = ref('')

    const loadUsers = async () => {
      try {
        users.value = await fetchUsers()
      } catch (error) {
        console.error('Error loading users:', error)
      }
    }

    const initMap = async () => {
      try {
        if (!document.getElementById('map')) {
          throw new Error('Map container not found')
        }

        await loadGoogleMaps()
        const mapElement = document.getElementById('map')
        const map = createMap(mapElement)

        map.addListener('click', async (event) => {
          if (!event.latLng) return

          const user = getCurrentUser()
          console.log('Current user:', user)
          if (!user) {
            alert('Please log in first. Redirecting you now...')
            router.push('/login')
            return
          }

          const marker = new window.google.maps.Marker({
            position: event.latLng,
            map: map,
            animation: window.google.maps.Animation.DROP,
          })

          markers.value.push(marker)

          const lat = event.latLng.lat()
          const lng = event.latLng.lng()
          console.log('Marker added at:', { lat, lng })

          const potholeData = {
            userId: user.id,
            latitude: lat,
            longitude: lng,
            ///description: 'Pothole reported via map click',
          }

          try {
            const response = await fetch('http://localhost:8080/api/pothole', {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json',
              },
              body: JSON.stringify(potholeData),
            })

            if (!response.ok) {
              throw new Error('Failed to report pothole')
            }
            console.log('Sending payload:', potholeData)

            alert('Pothole reported successfully!')
          } catch (error) {
            console.error('Error reporting pothole:', error)
            alert('Error reporting pothole:', error)
          }
        })
      } catch (error) {
        console.error('Error initializing map:', error)
        mapError.value = error.message
      }
    }

    const logout = () => {
      localStorage.removeItem('user') // or use your auth service method
      router.push('/login')
    }

    onMounted(() => {
      loadUsers()
      initMap()
      const user = getCurrentUser()
      console.log('Current user after mounted:', user)
    })

    return { users, mapError, userInput, logout }
  },
}
</script>

<template>
  <div>
    <h1>User List</h1>
    <ul>
      <li v-for="user in users" :key="user.id">{{ user.name }} - {{ user.email }}</li>
    </ul>

    <!-- Google Map container-->
    <div id="map" style="height: 400px; width: 100%; margin-top: 20px"></div>
  </div>

  <!-- New: Text Input -->
  <div style="margin-top: 20px">
    <label for="textInput">Enter Description:</label>
    <input
      v-model="userInput"
      id="textInput"
      type="text"
      placeholder="Write something..."
      style="margin-left: 10px; padding: 80px"
    />
  </div>

  <!-- New: Log Out Button -->
  <div style="margin-top: 20px">
    <button
      @click="logout"
      style="
        padding: 8px 16px;
        background-color: #f44336;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
      "
    >
      Log Out
    </button>
  </div>
</template>
