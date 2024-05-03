const { createApp, ref } = Vue

const currencyConvertorAPI = {
    fetch(sourceCurrency, targetCurrency, monetaryValue) {
        const url = new URL('/rates', window.location.origin);
        url.searchParams.append('source_currency', sourceCurrency);
        url.searchParams.append('target_currency', targetCurrency);
        url.searchParams.append('monetary_value', monetaryValue);

        return fetch(url)
            .then(response => {
                if (!response.ok) {
                    return response.json().then(error => {
                        throw error;
                    });
                }
                return response.json();
            });
    }
}

const ConversionResult = {
    props: ['rate'],
    template: '<div>{{rate}}</div>'
}

const app = createApp({
    data() {
        return {
            rate: '',
            sourceCurrency: '',
            targetCurrency: '',
            monetaryValue: '',
            errorMessage: ''
        }
    },
    components: {
        ConversionResult
    },
    methods: {
        fetchRate() {
            currencyConvertorAPI.fetch(this.sourceCurrency, this.targetCurrency, this.monetaryValue)
                .then(data => {
                    this.rate = data.result;
                    this.errorMessage = '';
                })
                .catch(error => {
                    this.rate = '';
                    this.errorMessage = Object.values(error.errors).flat().join('. ');
                    console.error('Error:', error);
                });
        }
    },
    template: `
        <div>
            <input v-model="sourceCurrency" placeholder="Source Currency">
            <input v-model="targetCurrency" placeholder="Target Currency">
            <input v-model="monetaryValue" placeholder="Monetary Value">
            <button @click="fetchRate">Fetch Rate</button>
            <ConversionResult :rate="rate"/>
            <div v-if="errorMessage">{{errorMessage}}</div>
        </div>
    `
})

app.mount('#app')