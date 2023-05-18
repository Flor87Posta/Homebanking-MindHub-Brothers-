const {createApp} = Vue

const app = createApp ({

    data(){
        return {
            firstName : '',
            lastName : '',
            email : '',
            clients : [],

            name: "",
            amount: "",
            checked: [],
            description: "",
            interest: "",

        }
    },
    created(){
    //         // Hacer una petición a través de una función loadData
            this.loadData();

            

            },

        methods: {

        loadData(){
                axios.get("http://localhost:8080/api/clients")
                    .then(response => { 
                        this.clients=response.data;
                        console.log(response)    
                        
                    
                    
                    })
                    .catch(err => console.log( err ));
                },

        addClient(){
            this.postClient();
        },

        postClient(){
                axios.post("http://localhost:8080/api/clients", {
                    firstName: this.firstName,
                    lastName: this.lastName,
                    email: this.email,
                })
                .then(function (response) {
                    this.loadData();
                })
                .catch(function (error) {
                    console.log(error);
                });
            },

            createLoan(){
                Swal.fire({
                    title: 'Are you sure that you want to create this loan?',
                    inputAttributes: {
                        autocapitalize: 'off'
                    },
                    showCancelButton: true,
                    confirmButtonText: 'Sure',
                    confirmButtonColor: "#7c601893",
                    preConfirm: () => {
                        return axios.post('/api/loans/admin-loan' , {
                            "name" : this.name,
                            "maxAmount" : this.amount,
                            "payments" : this.checked,
                            "descriptionLoan" : this.description,
                            "interest" : this.interest
                            })
                            .then(response => {
                                Swal.fire({
                                    icon: 'success',
                                    text: 'You create a new Loan',
                                    showConfirmButton: false,
                                    timer: 2000,
                                }).then( () => window.location.href='http://localhost:8080/manager.html')
                            })
                            .catch(error => {
                                Swal.fire({
                                    icon: 'error',
                                    text: error.response.data,
                                    confirmButtonColor: "#7c601893",
                                })
                            })
                    },
                    allowOutsideClick: () => !Swal.isLoading()
                })
    
            }
        

            },
    

/*    loadData: obtiene el listado de clientes usando AJAX al back-end o servicio REST.

    Realiza una petición HTTP de tipo GET a la URL /clients con la librería axios.

    Cuando la petición es respondida se ejecuta el método then

    El método then guarda en la data de Vue el listado de clientes que llega en el JSON así como el JSON completo.

    Vue se encarga de mostrar esos datos de manera automática, puedes volver a verificar el HTML para que veas las instrucciones de Vue que lo hacen.



    addClient: se ejecuta al pulsar el botón “add client” en la página web.

    Obtiene la data del formulario ya que los campos del mismo se encuentran asociados a la data de Vue.

    Si se han introducido los datos se ejecuta la función postClient



    postClient: obtiene los datos del nuevo cliente usando AJAX (peticiones asíncronas) al back-end o servicio REST.

    Realiza una petición HTTP de tipo POST a la URL /cients con la librería axios.

    Cuando la petición es respondida se ejecuta el método then

    El método then ejecuta la función loadData para que se recargue la información en la página.*/
})

app.mount('#app')