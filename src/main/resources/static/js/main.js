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
    props: ['result', 'errorMessage', 'monetaryValue', 'sourceCurrency', 'formatCurrency'],
    template: `
        <div>
            <div v-if="result" class="conversion-result">{{ formatCurrency(monetaryValue, sourceCurrency) }} equals {{result}}</div>
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
            currentSourceCurrency: '',
            currentTargetCurrency: '',
            currentMonetaryValue: '',
            errorMessage: ''
        }
    },
    components: {
        ConversionResult
    },
    methods: {
        fetchResult() {
            this.currentSourceCurrency = this.sourceCurrency;
            this.currentTargetCurrency = this.targetCurrency;
            this.currentMonetaryValue = this.monetaryValue;

            currencyConvertorAPI.fetch(this.currentSourceCurrency, this.currentTargetCurrency, this.currentMonetaryValue)
                .then(data => {
                    this.result = data.result;
                    this.errorMessage = '';
                })
                .catch(error => {
                    this.result = '';
                    let uniqueErrorMessages = new Set(Object.entries(error.errors).map(([key, messages]) => Array.isArray(messages) ? messages.map(message => `<p>${message}</p>`).join('') : `<p>${messages}</p>`));
                    this.errorMessage = Array.from(uniqueErrorMessages).join(' ');
                    console.error('Error:', error);
                });
        },
        formatCurrency(monetaryValue, sourceCurrency) {
            let userLanguage = navigator.language;
            console.log(userLanguage);
            return new Intl.NumberFormat(userLanguage, { style: 'currency', currency: sourceCurrency }).format(monetaryValue);
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
            <ConversionResult :result="result" :errorMessage="errorMessage" :monetaryValue="currentMonetaryValue" :sourceCurrency="currentSourceCurrency" :formatCurrency="formatCurrency"/>
        </div>
    `
})

app.mount('#app')