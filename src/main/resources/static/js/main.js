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
    props: ['result', 'errorMessage', 'monetaryValue', 'sourceCurrency'],
    template: `
        <div>
            <div v-if="result" class="conversion-result">{{monetaryValue}} {{sourceCurrency}} equals {{result}}</div>
            <div v-else-if="errorMessage" class="error-message"  v-html="errorMessage"></div>
            <div v-else>Conversion result will be displayed here...</div>
        </div>
    `
}

const app = createApp({
    data() {
        return {
            result: '',
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
        fetchResult() {
            currencyConvertorAPI.fetch(this.sourceCurrency, this.targetCurrency, this.monetaryValue)
                .then(data => {
                    this.result = data.result;
                    this.errorMessage = '';
                })
                .catch(error => {
                    this.result = '';
                    this.errorMessage = Object.values(error.errors).flat().map(message => `<p>${message}</p>`).join('');
                    console.error('Error:', error);
                });
        }
    },
    template: `
        <div>
            <h1>Currency Convertor</h1>
            <form @submit.prevent="fetchResult">
                <input v-model="sourceCurrency" placeholder="Source Currency">
                <input v-model="targetCurrency" placeholder="Target Currency">
                <input v-model="monetaryValue" placeholder="Monetary Value">
                <button type="submit">Convert</button>
            </form>
            <ConversionResult :result="result" :errorMessage="errorMessage" :monetaryValue="monetaryValue" :sourceCurrency="sourceCurrency"/>
        </div>
    `
})

app.mount('#app')