(function() {
    'use strict';

    angular
        .module('testEsApp')
        .controller('InstrumentDialogController', InstrumentDialogController);

    InstrumentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Instrument', 'Genre'];

    function InstrumentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Instrument, Genre) {
        var vm = this;

        vm.instrument = entity;
        vm.clear = clear;
        vm.save = save;
        vm.genres = Genre.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.instrument.id !== null) {
                Instrument.update(vm.instrument, onSaveSuccess, onSaveError);
            } else {
                Instrument.save(vm.instrument, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('testEsApp:instrumentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
