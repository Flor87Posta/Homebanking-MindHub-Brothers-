


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

            loans: [],
            id: "",
            idLoan:"",
            dataFilter: 0,
            quotas: 0,
            account: "",
            amount: "",
            totalPay: 0,

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
                    this.loans = this.client.loans.filter(loan => loan.finalAmount > 0);
                    console.log(this.loans);
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
                                this.loanSuccess = false;
                                this.loadData()
                                Swal.fire({
                                    icon: 'success',
                                    title: 'You have a new Loan!',
                                    showCancelButton: true,
                                    confirmButtonText: 'Accepted',
                                    cancelButtonText: 'Cancel',
                                    timer: 6000,
                                }).then( () => window.location.href="/web/accounts.html")
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



        isLoading() {
            this.loading = true;
        },

        showForm() {
            this.formVisible = true;
            this.mostrarFormulario1=false;
        },

        filterLoan(event) {
            this.dataFilter = this.loans.find(loan => loan.name === event.target.nextElementSibling.textContent.toUpperCase());
            console.log(event.target.nextElementSibling.textContent)
            console.log(this.dataFilter);
            this.quotas = this.dataFilter.finalAmount / this.dataFilter.payments;
            this.totalPay = this.dataFilter.finalAmount;
        },

        payLoan(){
            Swal.fire({
                title: 'Are you sure that you want to pay the loan?',
                inputAttributes: {
                    autocapitalize: 'off'
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                confirmButtonColor: "#7c601893",
                preConfirm: () => {
                    return axios.post('/api/clients/current/pay-loan', `idLoan=${this.dataFilter.id}&account=${this.account}&amount=${this.amount}`)
                    .then(response => {
                            Swal.fire({
                                icon: 'success',
                                text: 'Payment Success',
                                showConfirmButton: false,
                                timer: 2000,
                            }).then( () => window.location.href="/web/accounts.html")
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
        },






    }
}).mount("#app")