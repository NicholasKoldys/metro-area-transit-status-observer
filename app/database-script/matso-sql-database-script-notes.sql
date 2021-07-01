
SELECT * FROM U05iV3.user;
-- userId, userName, password, active, loggedIn, lastLogin, attempt, createDate, createdBy, lastUpdate, lastUpdateBy
SELECT * FROM U05iV3.buses;
-- busId, busNum, active, capacity, routeId, maintenanceState, lastBreakdown, createDate, createdBy, lastUpdate, lastUpdateBy
SELECT * FROM U05iV3.routes;
-- routeId, routeNum, startPoint, endPoint, stopsTotal, timeLength, distance, currentDelay, customersAvg, createDate, createdBy, lastUpdate, lastUpdateBy
SELECT * FROM U05iV3.busChart;
-- busChartId, routeId, busId

-- SELECT busId,
--  busNum,
--  active,
--  capacity,
--  U05iV3.buses.routeId,
--  maintenanceState, 
--  lastBreakdown, 
--  U05iV3.routes.routeNum 
-- FROM U05iV3.buses LEFT OUTER JOIN U05iV3.routes 
-- ON U05iV3.buses.routeId = U05iV3.routes.routeId
-- WHERE active = 1;

-- SELECT Count(*), maintenanceState
-- FROM U05iV3.buses
-- group by maintenanceState;

-- SELECT routeNum, SUM(capacity), customersAvg
-- FROM U05iV3.routes LEFT OUTER JOIN U05iV3.buses
-- ON U05iV3.routes.routeId = U05iV3.buses.routeId
-- GROUP BY routeNum, customersAvg;

-- SELECT U05iV3.buses.*, routeNum
-- FROM U05iV3.buses LEFT OUTER JOIN U05iV3.routes
-- ON U05iV3.buses.routeId = U05iV3.routes.routeId;

-- SELECT U05iV3.busChart.routeId,
-- 		U05iV3.routes.routeNum,
-- 		U05iV3.busChart.busId,
-- 		U05iV3.buses.busNum 
-- FROM U05iV3.busChart, U05iV3.routes, U05iV3.buses
-- WHERE U05iV3.busChart.routeId IS NOT NULL 
-- 		AND U05iV3.busChart.busId IS NOT NULL 
--         AND U05iV3.busChart.busId = U05iV3.buses.busId 
--         AND U05iV3.busChart.routeId = U05iV3.routes.routeId
-- ;

-- SELECT U05iV3.routes.routeNum as rRouteNum,
-- 		U05iV3.routes.customersAvg as rCustomerAvg,
--         SUM(U05iV3.buses.capacity) as total_capacity
-- FROM U05iV3.routes, U05iV3.buses
-- WHERE U05iV3.routes.routeId = U05iV3.buses.routeId AND U05iV3.buses.active = 1
-- GROUP BY rRouteNum, rCustomerAvg
-- HAVING total_capacity < rCustomerAvg;

-- SELECT U05iV3.routes.routeNum as rRouteNum,
-- 		U05iV3.routes.customersAvg as rCustomerAvg,
--         SUM(U05iV3.buses.capacity) as total_capacity
-- FROM U05iV3.routes, U05iV3.buses
-- WHERE U05iV3.routes.routeId = U05iV3.buses.routeId AND U05iV3.buses.active = 1
-- GROUP BY rRouteNum, rCustomerAvg
-- HAVING total_capacity > rCustomerAvg;


UPDATE U05iV3.user
SET loggedIn = 0;



-- TRUNCATE TABLE U05iV3.user;
-- ALTER TABLE U05iV3.user AUTO_INCREMENT = 1;
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE U05iV3.buses;
ALTER TABLE U05iV3.buses AUTO_INCREMENT = 1;
TRUNCATE TABLE U05iV3.routes;
ALTER TABLE U05iV3.routes AUTO_INCREMENT = 1;
TRUNCATE TABLE U05iV3.busChart;
ALTER TABLE U05iV3.busChart AUTO_INCREMENT = 1;
SET FOREIGN_KEY_CHECKS = 1;


-- INSERT INTO U05iV3.user
-- (userName, password, active, loggedIn, lastLogin, attempt, createDate, createdBy, lastUpdate, lastUpdateBy)
-- VALUES ("ADMIN", sha1("admin"), 1, 0, NOW(), 0, NOW(), "ADMIN", NOW(), "ADMIN");
-- INSERT INTO U05iV3.user
-- (userName, password, active, loggedIn, lastLogin, attempt, createDate, createdBy, lastUpdate, lastUpdateBy)
-- VALUES ("nick", sha1("password"), 1, 0, NOW(), 0, NOW(), "ADMIN", NOW(), "ADMIN");


-- INSERT INTO U05iV3.routes
-- (routeId, routeNum, startPoint, endPoint, stopsTotal, timeLength, distance, currentDelay,
--  customersAvg, createDate, createdBy, lastUpdate, lastUpdateBy)
-- VALUES (1, "000", "HWY 66 S", "HWY ??? Simple St", 1, TIME("00:00:00"), 999.99, -TIME("00:00:00"),
--  00, NOW(), "ADMIN", NOW(), "ADMIN");
--  INSERT INTO U05iV3.routes
-- (routeId, routeNum, startPoint, endPoint, stopsTotal, timeLength, distance, currentDelay,
--  customersAvg, createDate, createdBy, lastUpdate, lastUpdateBy)
-- VALUES (2, "000", "HWY 66 S", "HWY ??? Simple St", 1, TIME("00:00:00"), 999.99, -TIME("00:00:00"),
--  00, NOW(), "ADMIN", NOW(), "ADMIN");
--  INSERT INTO U05iV3.routes
-- (routeId, routeNum, startPoint, endPoint, stopsTotal, timeLength, distance, currentDelay,
--  customersAvg, createDate, createdBy, lastUpdate, lastUpdateBy)
-- VALUES (3, "000", "HWY 66 S", "HWY ??? Simple St", 1, TIME("00:00:00"), 999.99, -TIME("00:00:00"),
--  00, NOW(), "ADMIN", NOW(), "ADMIN");
--  INSERT INTO U05iV3.routes
-- (routeId, routeNum, startPoint, endPoint, stopsTotal, timeLength, distance, currentDelay,
--  customersAvg, createDate, createdBy, lastUpdate, lastUpdateBy)
-- VALUES (4, "000", "HWY 66 S", "HWY ??? Simple St", 1, TIME("00:00:00"), 999.99, -TIME("00:00:00"),
--  00, NOW(), "ADMIN", NOW(), "ADMIN");



-- INSERT INTO U05iV3.buses
-- (busNum, active, capacity, routeId, maintenanceState, lastBreakdown, createDate, createdBy, lastUpdate, lastUpdateBy)
-- VALUES ("001A", 1, 45, null, "OK", NOW(), NOW(), "ADMIN", NOW(), "ADMIN");
-- INSERT INTO U05iV3.buses
-- (busNum, active, capacity, routeId, maintenanceState, lastBreakdown, createDate, createdBy, lastUpdate, lastUpdateBy)
-- VALUES ("002A", 1, 45, null, "SERVICE", NOW(), NOW(), "ADMIN", NOW(), "ADMIN");
-- INSERT INTO U05iV3.buses
-- (busNum, active, capacity, routeId, maintenanceState, lastBreakdown, createDate, createdBy, lastUpdate, lastUpdateBy)
-- VALUES ("003A", 1, 45, null, "OUT_OF_ORDER", NOW(), NOW(), "ADMIN", NOW(), "ADMIN");




DELIMITER  \\
CREATE TRIGGER U05iV3.chart_INS_bus
AFTER INSERT ON U05iV3.buses FOR EACH ROW
BEGIN
	IF new.active = 1 THEN
		INSERT INTO U05iV3.busChart (routeId, busId) VALUES (new.routeId, new.busId);
	END IF;
    
    IF ((SELECT count(routeId) FROM busChart WHERE routeId = new.routeId AND busId IS NULL) >= 1 AND
				(SELECT count(routeId) FROM busChart WHERE routeId = new.routeId AND busId IS NOT NULL) >= 1) THEN
		DELETE FROM U05iV3.busChart 
        WHERE busId IS NULL AND routeId = new.routeId;
	END IF;
END \\
DELIMITER ;



DELIMITER  \\
CREATE TRIGGER U05iV3.chart_UPD_bus
AFTER UPDATE ON U05iV3.buses FOR EACH ROW
BEGIN
	IF (old.active = 0 AND new.active = 1) THEN 
		INSERT INTO U05iV3.busChart 
        (routeId, busId) 
        VALUES (new.routeId, old.busId);
    ELSEIF (IFNULL(old.routeId, 0) <> IFNULL(new.routeId, 0) AND new.active = 1) THEN 
		UPDATE U05iV3.busChart 
        SET routeId = new.routeId 
        WHERE busId = old.busId;
	END IF;
    
	IF (old.active = 1 AND new.active = 0) THEN
		DELETE FROM U05iV3.busChart 
        WHERE busID = old.busId;
	END IF;

    -- Balance out Bus Chart / Allow Routes to show if they have no buses assigned
	IF ((SELECT count(routeId) FROM busChart WHERE routeId = old.routeId) = 0) THEN
		INSERT INTO U05iV3.busChart 
        (routeId) 
        VALUES (old.routeId);
	END IF;
    
	IF ((SELECT count(routeId) FROM busChart WHERE routeId = new.routeId AND busId IS NULL) >= 1 AND
				(SELECT count(routeId) FROM busChart WHERE routeId = new.routeId AND busId IS NOT NULL) >= 1) THEN
		DELETE FROM U05iV3.busChart 
        WHERE busId IS NULL AND routeId = new.routeId;
	END IF;
    
    If ((SELECT count(busChartId) FROM busChart WHERE routeId IS NULL AND busID IS NULL) >=1 ) THEN
		DELETE FROM U05iV3.busChart
        WHERE busId IS NULL AND routeId IS NULL;
	END IF;
END \\
DELIMITER ;



DELIMITER  \\
CREATE TRIGGER U05iV3.chart_INS_route
AFTER INSERT ON U05iV3.routes FOR EACH ROW
BEGIN
    IF (SELECT COUNT(routeId) FROM U05iV3.busChart WHERE routeId = new.routeId) >= 1 THEN 
		DO null;
    ELSE 
		INSERT INTO U05iV3.busChart (routeId) VALUES (new.routeId);
    END IF;
END \\
DELIMITER ;



-- -- NO NEED FOR UPDATE ON ROUTES - ROUTES ARE NEVER DELETED AND DO NOT HAVE FK WITH BUSES
-- DELIMITER  \\
-- CREATE TRIGGER U05iV3.chart_UPD_route
-- AFTER UPDATE ON U05iV3.routes FOR EACH ROW
-- BEGIN
--     IF new.routeId <> old.routeId THEN
-- 		UPDATE U05iV3.busChart SET routeId = new.routeId WHERE routeId = old.routeId;
--     END IF;
-- END \\
-- DELIMITER ;



DROP TRIGGER U05iV3.chart_INS_bus;
DROP TRIGGER U05iV3.chart_UPD_bus;
DROP TRIGGER U05iV3.chart_INS_route;
DROP TRIGGER U05iV3.chart_UPD_route;

SET SQL_SAFE_UPDATES = 0;
-- SET FOREIGN_KEY_CHECKS = 0;
-- TRUNCATE TABLE U05iV3.user;
-- SET FOREIGN_KEY_CHECKS = 1;