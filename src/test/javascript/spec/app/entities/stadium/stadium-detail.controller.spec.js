'use strict';

describe('Stadium Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockStadium, MockTeam;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockStadium = jasmine.createSpy('MockStadium');
        MockTeam = jasmine.createSpy('MockTeam');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Stadium': MockStadium,
            'Team': MockTeam
        };
        createController = function() {
            $injector.get('$controller')("StadiumDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'pruebaApp:stadiumUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
