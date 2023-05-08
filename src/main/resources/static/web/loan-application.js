


const { createApp } = Vue
createApp({


    data() {
        return {

            client: undefined,
            clientAccounts: [],
            requestedAmount: 0,
            requestedPayments: 0,
            loanDTO: undefined,
            loanId: "",
            destinationAccNumber: null,
            loanSuccess: false,
            loanError: false,
            loading: true,
            loanErrorMessage: null,
            formVisible: false,
            mostrarFormulario1:true,
            amountInterest:0,
            paymentsFinal:0,
        
        

            loanTypeMORTGAGE: [],
            MORTGAGE:[],
            paymentsMORTGAGE:[],
            PERSONAL:[],
            paymentsPERSONAL:[],
            AUTO:[],
            paymentsAUTO:[],

        }
    },


    created() {
        this.loadData()

    },

    methods: {
        loadData() {
            this.isLoading()
            axios
                .get(`/api/clients/current`)
                .then(data => {
                    console.log(data);
                    this.client = data.data;
                    console.log(this.client);
                    this.sortAccounts()
                }
                )
                .catch(error => {
                    console.log(error);
                })
        },


        sortAccounts() {
            let sortedAccounts = this.client.accounts.sort((acc1, acc2) => acc1.id - acc2.id)
            this.clientAccounts = sortedAccounts
        },
        logout(){
            axios.post('/api/logout')
            .then(response => console.log('Signed out'))
            
        },

        requestLoan() {
        
            Swal.fire({
                icon: 'warning',
                title: 'You are requesting a new Loan..¿Are you sure?',
                showCancelButton: true,
                confirmButtonText: 'Yes, request new Loan',
                cancelButtonText: 'Cancel',
                timer: 6000,
            }).then((result) => {
                if (result.isConfirmed) {
            axios.post('/api/clients/current/loans', {
                loanId: this.loanId,
                amount: this.requestedAmount,
                payments: this.requestedPayments,
                destinationAccNumber: this.destinationAccNumber
            }).then(response => {
                    if (response.status === 201) {
                                this.loanSucces = false;
                                this.loadData()
                                Swal.fire({
                                    icon: 'success',
                                    title: 'You have a new Loan!',
                                    showCancelButton: true,
                                    confirmButtonText: 'Accepted',
                                    cancelButtonText: 'Cancel',
                                    timer: 6000,
                                })
                        }
                    
                }).catch(error => Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: error.response.data,
                    timer: 6000,
                }))
            }
                }
                )
        
        },

        getLoanDTO() {
            console.log(this.loanId)
            axios.get(`/api/loans`)
                .then(r => {
                    console.log(r)
                    this.loanDTO = r.data;
                    console.log(this.loanDTO);

                    this.MORTGAGE=r.data[0];
                    console.log(this.MORTGAGE);
                    this.paymentsMORTGAGE=this.MORTGAGE.payments;
                    console.log(this.paymentsMORTGAGE)
                    this.loanTypeMORTGAGE=this.MORTGAGE.name
                    console.log(this.loanTypeMORTGAGE)

                    this.PERSONAL=r.data[1];
                    console.log(this.PERSONAL);
                    this.paymentsPERSONAL=this.PERSONAL.payments;
                    console.log(this.paymentsPERSONAL)
                    this.loanTypePERSONAL=this.PERSONAL.name
                    console.log(this.loanTypePERSONAL)

                    this.AUTO=r.data[2];
                    console.log(this.AUTO);
                    this.paymentsAUTO=this.AUTO.payments;
                    console.log(this.paymentsAUTO)
                    this.loanTypeAUTO=this.AUTO.name
                    console.log(this.loanTypeAUTO)
                    
                })
        },

        // interestRatio(){
        
        //     this.amountInterest = this.requestedAmount * 1.2;
        //     this.rateInterest = this.amountInterest
        //     this.paymentsFinal = this.amountInterest / this.requestedPayments;
        // },



        isLoading() {
            this.loading = true;
        },

        showForm() {
            this.formVisible = true;
            this.mostrarFormulario1=false;
        },






    }
}).mount("#app")