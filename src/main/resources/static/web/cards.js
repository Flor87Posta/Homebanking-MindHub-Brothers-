
const {createApp} = Vue


const app = createApp ({

    data(){
        return {
            firstName : '',
            lastName : '',
            accounts: [],
            clients: [],
            loans: [],
            cards: [],
            debitCards: [],
            creditCards: [],

            

        }
    },
    created(){
    //         // Hacer una petición a través de una función loadData
            this.loadData();
    

            },

        methods: {

        loadData(){
                axios.get("http://localhost:8080/api/clients/1")
                    .then(response => {
                        this.clients=response.data;
                        console.log(this.clients)
                        this.accounts=this.clients.accounts
                        this.loans=this.clients.loans
                        this.cards=this.clients.cards
                        this.debitCards = this.cards.filter(card => card.typeCard === "DEBIT")
                        console.log(this.debitCards)
                        this.creditCards = this.cards.filter(card => card.typeCard === "CREDIT")
                        console.log(this.creditCards)



                    })
                    .catch(err => console.log( err ));
                },

        addClient(){
            this.postClient();
        },

        postClient(){
                axios.post("http://localhost:8080/api/clients/1", {
                    firstName: this.firstName,
                    lastName: this.lastName,
                    email: this.email,
                    accounts: this.accounts,

                    
                })
                .then(function (response) {
                    this.loadData();
                })
                .catch(function (error) {
                    console.log(error);
                });
            },

            },

})

app.mount('#app')