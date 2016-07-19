'use strict';

describe('Referee Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockReferee, MockLeague, MockGame;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockReferee = jasmine.createSpy('MockReferee');
        MockLeague = jasmine.createSpy('MockLeague');
        MockGame = jasmine.createSpy('MockGame');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Referee': MockReferee,
            'League': MockLeague,
            'Game': MockGame
        };
        createController = function() {
            $injector.get('$controller')("RefereeDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'pruebaApp:refereeUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
