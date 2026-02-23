CREATE TABLE `etapa_plan_ambiental` (
  `codigo_etapa` bigint(20) NOT NULL,
  `titulo_etapa` varchar(255) DEFAULT NULL,
  `fk_cod_plan_ambiental` bigint(20) DEFAULT NULL,
  `fecha_creado_etapa` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_editado_etapa` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`codigo_etapa`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Consulta base para consumir el catalogo en dropdowns.
SELECT codigo_etapa, titulo_etapa
FROM etapa_plan_ambiental
ORDER BY codigo_etapa;
