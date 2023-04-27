
const {createApp} = Vue


const app = createApp ({

    data(){
        return {
            firstName : '',
            lastName : '',
            accounts: [],
            clients: [],
            loans: [],
            createdAccount: false,
            condicion: true,
            
            

            

        }
    },
    created(){
    //         // Hacer una petición a través de una función loadData
            // this.newAccount();
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
                        this.condicion = this.accounts.length<=3




                    })
                    .catch(err => console.log( err ));
                },

        // addClient(){
        //     this.postClient();
        // },

        // postClient(){
        //         axios.post("http://localhost:8080/api/clients/1", {
        //             firstName: this.firstName,
        //             lastName: this.lastName,
        //             email: this.email,
        //             accounts: this.accounts,

                    
        //         })
        //         .then(function (response) {
        //             this.loadData();
        //         })
        //         .catch(function (error) {
        //             console.log(error);
        //         });
        //     },

        logOut(){
                axios.post('/api/logout')
                .then(response => console.log('Signed out'))
            },


        newAccount() {Swal.fire({
            icon: 'warning',
            title: 'You are creating a new Account..¿Are you sure?',
            showCancelButton: true,
            confirmButtonText: 'Yes, created new Account',
            cancelButtonText: 'Cancell',
            timer: 6000,
          })
            axios.post('/api/clients/current/accounts')
                .then(response => {
                    if (response.status == "201") {
                            this.createdAccount = true,
                            this.loadData()
                    }
                })
                .catch(error => {
                    console.log(error);
                    if (error.code == "ERR_BAD_REQUEST") {
                        console.log(error)
                    }
                })
        },

        button() {
            // Lógica para enviar el formulario
            
            Swal.fire({
              icon: 'success',
              title: '¡Formulario enviado!',
              text: 'Gracias por enviar el formulario.',
              confirmButtonText: 'Aceptar'
            })
          }

            },

})

app.mount('#app')