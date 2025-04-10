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

        map.addListener('click', (event) => {
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
        })
      } catch (error) {
        mapError.value = `Failed to initialize map: ${error.message}`
        console.error('Map initialization error:', error)
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
