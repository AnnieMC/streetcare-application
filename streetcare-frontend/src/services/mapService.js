//let googleMapsLoaded = false

export const loadGoogleMaps = () => {
  return new Promise((resolve, reject) => {
    if (window.google && window.google.maps) {
      resolve(window.google)
      return
    }
    window.initMap = () => {
      if (window.google && window.google.maps) {
        //googleMapsLoaded = true
        resolve(window.google)
      } else {
        reject(new Error('Google Maps not available after initialization'))
      }
    }

    try {
      const script = document.createElement('script')
      const apiKey = import.meta.env.VITE_GOOGLE_MAPS_API_KEY

      if (!apiKey) {
        throw new Error('Google Maps API key not found')
      }
      script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&callback=initMap`
      script.async = true // The script will be loaded asynchronously
      script.defer = true // The script will be executed after the page has loaded
      script.loading = 'async' // For browsers that support loading attribute

      script.onerror = () => reject(new Error('Google Maps API failed to load'))
      document.head.appendChild(script)
    } catch (error) {
      reject(error)
    }
  })
}

export const createMap = (element, options = {}) => {
  if (!element) {
    throw new Error('Google Maps API not loaded')
  }

  if (!window.google || !window.google.maps) {
    throw new Error('Google Maps API not loaded. Call loadGoogleMaps() first')
  }

  const defaultOptions = {
    center: { lat: 53.4721321, lng: -2.3882661 }, // Manchester coordinates
    zoom: 8,
    mapTypeId: window.google.maps.MapTypeId.ROADMAP,
    fullscreenControl: true,
    streetViewControl: true,
  }

  return new window.google.maps.Map(element, { ...defaultOptions, ...options })
}
