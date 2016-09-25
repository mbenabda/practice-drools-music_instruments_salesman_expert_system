(function() {
    'use strict';

    angular
        .module('testEsApp')
        .controller('InstrumentController', InstrumentController);

    InstrumentController.$inject = ['$scope', '$state', 'Instrument'];

    function InstrumentController ($scope, $state, Instrument) {
        var vm = this;
        
        vm.instruments = [];

        loadAll();

        function loadAll() {
            Instrument.query(function(result) {
                vm.instruments = result;
            });
        }
    }
})();
