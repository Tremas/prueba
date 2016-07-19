'use strict';

describe('League Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockLeague, MockSeason, MockReferee;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockLeague = jasmine.createSpy('MockLeague');
        MockSeason = jasmine.createSpy('MockSeason');
        MockReferee = jasmine.createSpy('MockReferee');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'League': MockLeague,
            'Season': MockSeason,
            'Referee': MockReferee
        };
        createController = function() {
            $injector.get('$controller')("LeagueDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'pruebaApp:leagueUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
