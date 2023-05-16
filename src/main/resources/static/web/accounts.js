
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
            accountType: "",
            formVisible: false,
            mostrarFormulario1:true,
            
            

            

        }
    },
    created(){
    //         // Hacer una petición a través de una función loadData
            // this.newAccount();
            this.loadData();
        
    

            },

        methods: {

        loadData(){
                 axios.get("http://localhost:8080/api/clients/current") //,{headers:{'accept':'application/xml'}}
                    .then(response => {
                        this.clients=response.data;
                        console.log(this.clients)
                    
                        this.accounts=this.clients.accounts
                        console.log(this.accounts)
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

        logout(){
                axios.post('/api/logout')
                .then(response => console.log('Signed out'))
                
            },


            newAccount() {
                console.log("hola")
                Swal.fire({
                    icon: 'warning',
                    title: 'You are creating a new Account..¿Are you sure?',
                    showCancelButton: true,
                    confirmButtonText: 'Yes, create new Account',
                    cancelButtonText: 'Cancel',
                    timer: 6000,
                }).then((result) => {
                    if (result.isConfirmed) {
                        axios.post('/api/clients/current/accounts',`accountType=${this.accountType}`)
                            .then(response => {
                                if (response.status == "201") {
                                        this.createdAccount = true,
                                        this.loadData()
                                        Swal.fire({
                                            icon: 'success',
                                            title: 'You have a new Account!',
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

            showForm() {
                this.formVisible = true;
                this.mostrarFormulario1=false;
            },
    
            },

})

app.mount('#app')