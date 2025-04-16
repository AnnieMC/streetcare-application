<script>
import { ref, onMounted } from 'vue'
import { fetchUsers } from '@/services/authService'
import { createMap, loadGoogleMaps } from '@/services/mapService'

export default {
  setup() {
    const users = ref([])
    const mapError = ref(null)
    const markers = ref([])

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

          const marker = new window.google.maps.Marker({
            position: event.latLng,
            map: map,
            animation: window.google.maps.Animation.DROP,
          })

          markers.value.push(marker)

          const lat = event.latLng.lat()
          const lng = event.latLng.lng()
          console.log('Marker added at:', { lat, lng })

          //Get logged-in user
          const user = JSON.parse(localStorage.getItem('user'))
          if (user) {
            alert('Please log in first.')
            return
          }

          const potholeData = {
            userId: user.id,
            lat: lat,
            lng: lng,
            description: 'Pothole reported via map click',
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

    onMounted(() => {
      loadUsers()
      initMap()
    })

    return { users, mapError }
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
</template>
