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
            createdCard: false,
            typeCardCreated: null,
            colorCard: null,
            requestedNumber: "",
            numberCreditCard: [],
            numberDebitCard:[],
            formVisible: false,
            mostrarFormulario1:true,

            paymentDTO: {
                typeCard: '',
                number: '',
                amount: 0,
                description: '',
                cvv: ''
            }

            

        }
    },
    created(){
    //         // Hacer una petición a través de una función loadData
            this.loadData();
    

            },

        methods: {

        loadData(){
                axios.get("http://localhost:8080/api/clients/current")
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
                        this.numberCreditCard = this.creditCards.number
                        console.log(this.numberCreditCard)
                        this.numberDebitCard = this.debitCards.number
                        console.log(this.numberDebitCard)



                    })
                    .catch(err => console.log( err ));
                },



            logout(){
                axios.post('/api/logout')
                .then(response => console.log('Signed out'))
            },


                

                showForm() {
                            this.formVisible = true;
                            this.mostrarFormulario1=false;
                            },

                payWithCard() {
                    Swal.fire({
                        icon: 'warning',
                        title: 'You are making a pay in a Card..¿Are you sure?',
                        showCancelButton: true,
                        confirmButtonText: 'Yes, pay Card',
                        cancelButtonText: 'Cancel',
                        timer: 6000,
                    }).then((result) => {
                        if (result.isConfirmed) {
                        axios.post('/clients/current/pay-card', this.paymentDTO)
                        .then(response => {
                            if (response.status == "201") {
                                console.log(response),
                                window.location.href='/web/account.html',
                                this.loadData()
                                Swal.fire({
                                    icon: 'Success!',
                                    title: 'You pay a Card!',
                                    showCancelButton: true,
                                    confirmButtonText: 'Accepted',
                                    cancelButtonText: 'Cancel',
                                    timer: 6000,
                                })
                            }  
                        })
                        .catch(error => Swal.fire({
                            icon: 'error',
                            title: 'Error',
                            text: error.response.data,
                            timer: 6000,
                        }))
                            }
                                })
                                    },

            },

})

app.mount('#app')